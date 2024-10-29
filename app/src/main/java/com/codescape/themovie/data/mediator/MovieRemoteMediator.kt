package com.codescape.themovie.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.codescape.themovie.data.api.service.MovieApiService
import com.codescape.themovie.data.db.MoviesDatabase
import com.codescape.themovie.data.db.model.MovieDb
import com.codescape.themovie.data.db.model.MovieGenreDb
import com.codescape.themovie.data.db.model.RemoteKeyDb
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit.Companion.HOUR
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until
import retrofit2.HttpException
import java.io.IOException

private const val LAST_PAGE_OF_PAGING = -2

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val query: Map<String, String>,
    private val database: MoviesDatabase,
    private val apiService: MovieApiService,
    private val cacheTimeout: Long
) : RemoteMediator<Int, MovieDb>() {
    private val movieDao = database.getMovieDao()
    private val movieGenreDao = database.getMovieGenreDao()
    private val remoteKeyDao = database.getRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val now = Clock.System.now()
        val period =
            movieDao.lastUpdated()?.until(
                other = now,
                unit = HOUR,
                timeZone = TimeZone.currentSystemDefault()
            ) ?: cacheTimeout
        return if (period >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieDb>
    ): MediatorResult {
        return try {
            val loadKey =
                when (loadType) {
                    LoadType.REFRESH -> 1
                    LoadType.PREPEND -> return MediatorResult.Success(
                        endOfPaginationReached = true
                    )

                    LoadType.APPEND -> {
                        val remoteKey =
                            database.withTransaction {
                                remoteKeyDao.remoteKeyByQuery(query = query)
                            }
                        if (remoteKey?.nextPage == LAST_PAGE_OF_PAGING) {
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        remoteKey?.nextPage?.plus(1)
                    }
                }
            val response = apiService.getMovies(query = query, page = loadKey ?: 1)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByQuery(query = query)
                    movieDao.deleteByQuery(query = query)
                }
                remoteKeyDao.upsert(
                    RemoteKeyDb(
                        query = query,
                        nextPage =
                            if (response.results.isEmpty()) LAST_PAGE_OF_PAGING else (loadKey ?: 1)
                    )
                )
                response.results.forEach {
                    movieDao.upsert(it.toDb(query = query))
                    movieGenreDao.upsert(
                        it.genreIds.map { genreId ->
                            MovieGenreDb(
                                movieId = it.id,
                                genreId = genreId
                            )
                        }
                    )
                }
            }
            MediatorResult.Success(endOfPaginationReached = response.results.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
