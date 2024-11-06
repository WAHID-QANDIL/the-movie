package com.codescape.themovie.presentation.home.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.modifier.conditional
import com.codescape.themovie.presentation.modifier.sharedTransition
import com.codescape.themovie.presentation.shared.SharedElementKey
import com.codescape.themovie.presentation.shared.SharedElementType
import com.codescape.themovie.presentation.theme.TheMovieTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier.Companion,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    origin: String = "",
    secondOrigin: String? = null,
    movie: Movie
) {
    val isPreview = LocalInspectionMode.current
    Box(modifier = modifier) {
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(movie.posterPath)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
            contentScale = ContentScale.Companion.Crop,
            contentDescription = "Icon",
            modifier =
                Modifier.Companion
                    .conditional(
                        condition = isPreview,
                        ifTrue = {
                            background(TheMovieTheme.colors.outline)
                        }
                    ).sharedTransition(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    ) { sharedTransitionScope, animatedVisibilityScope ->
                        with(sharedTransitionScope) {
                            sharedElement(
                                state =
                                    rememberSharedContentState(
                                        key =
                                            SharedElementKey(
                                                id = movie.id,
                                                origin = origin,
                                                type = SharedElementType.POSTER
                                            )
                                    ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform =
                                    BoundsTransform { initialBounds, targetBounds ->
                                        keyframes {
                                            durationMillis = 1000
                                            initialBounds at 0
                                            targetBounds at 1000
                                        }
                                    }
                            )
                        }
                    }.sharedTransition(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    ) { sharedTransitionScope, animatedVisibilityScope ->
                        with(sharedTransitionScope) {
                            if (secondOrigin != null) {
                                sharedElement(
                                    state =
                                        rememberSharedContentState(
                                            key =
                                                SharedElementKey(
                                                    id = movie.id,
                                                    origin = secondOrigin,
                                                    type = SharedElementType.POSTER
                                                )
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform =
                                        BoundsTransform { initialBounds, targetBounds ->
                                            keyframes {
                                                durationMillis = 1000
                                                initialBounds at 0
                                                targetBounds at 1000
                                            }
                                        }
                                )
                            } else {
                                this@sharedTransition
                            }
                        }
                    }.fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MovieCardPreview() {
    TheMovieTheme {
        MovieCard(
            modifier =
                Modifier.size(
                    width = 100.dp,
                    height = 100.dp
                ),
            sharedTransitionScope = null,
            animatedVisibilityScope = null,
            movie =
                Movie(
                    id = 1,
                    title = "Title",
                    overview = "Overview",
                    posterPath = "https://www.google.com",
                    backdropPath = "https://www.google.com",
                    releaseDate = "2023-01-01",
                    voteAverage = 1.0,
                    voteCount = 1,
                    popularity = 1.0,
                    adult = false,
                    originalLanguage = "en",
                    originalTitle = "Original Title",
                    video = false,
                    query = emptyMap()
                )
        )
    }
}
