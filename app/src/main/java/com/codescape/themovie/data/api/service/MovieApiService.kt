package com.codescape.themovie.data.api.service

import com.codescape.themovie.data.api.model.MovieResponseDto
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MovieApiService {
    @GET("/3/discover/movie")
    suspend fun getMovies(
        @QueryMap query: Map<String, String>,
        @Query("page") page: Int
    ): MovieResponseDto
}
