package com.codescape.themovie.presentation.details

import androidx.compose.animation.EnterExitState
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.codescape.themovie.R
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.details.preview.DetailsPreviewParametersProvider
import com.codescape.themovie.presentation.modifier.conditional
import com.codescape.themovie.presentation.modifier.sharedTransition
import com.codescape.themovie.presentation.shared.LocalAnimatedVisibilityScope
import com.codescape.themovie.presentation.shared.LocalSharedTransitionScope
import com.codescape.themovie.presentation.shared.SharedElementKey
import com.codescape.themovie.presentation.shared.SharedElementType
import com.codescape.themovie.presentation.theme.TheMovieTheme
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel,
    movie: Movie,
    origin: String,
    onClickBack: () -> Unit
) {
    LaunchedEffect(movie) {
        viewModel.updateMovie(movie)
    }
    val isFavoriteMovie by viewModel.isFavoriteMovie.collectAsStateWithLifecycle()
    DetailsScreenContent(
        modifier = modifier,
        isFavoriteMovie = isFavoriteMovie,
        movie = movie,
        origin = origin,
        onClickBack = onClickBack,
        onClickFavorite = {
            if (isFavoriteMovie) {
                viewModel.deleteFavoriteMovie(movie)
            } else {
                viewModel.saveFavoriteMovie(movie)
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationSpecApi::class)
@Composable
fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    isFavoriteMovie: Boolean,
    movie: Movie,
    origin: String = "",
    onClickBack: () -> Unit = {},
    onClickFavorite: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val sharedTransitionScope = LocalSharedTransitionScope.current
        val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
        val isPreview = LocalInspectionMode.current
        val backgroundColor = TheMovieTheme.colors.background
        val cornerSize =
            animatedVisibilityScope?.transition?.animateDp(
                label = "corner"
            ) { exitEnter ->
                when (exitEnter) {
                    EnterExitState.PreEnter -> 16.dp
                    EnterExitState.Visible -> 24.dp
                    EnterExitState.PostExit -> 24.dp
                }
            }
        val imageKey = remember { movie.id.toString() }
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(movie.backdropPath)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(true)
                    .build(),
            contentScale = ContentScale.Companion.Crop,
            contentDescription = "Icon",
            modifier =
                Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .graphicsLayer { alpha = 0.99f }
                    .drawWithContent {
                        val colors =
                            listOf(
                                backgroundColor,
                                Color.Transparent
                            )
                        drawContent()
                        drawRect(
                            brush =
                                Brush.linearGradient(
                                    colors = colors,
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, size.height)
                                ),
                            blendMode = BlendMode.DstIn
                        )
                    }
        )
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp
                    )
        ) {
            val (backRef, titleRef, posterRef, releaseDateRef, overviewRef, favoriteRef) = createRefs()
            Icon(
                modifier =
                    Modifier
                        .sharedTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        ) { sharedTransitionScope, animatedVisibilityScope ->
                            with(sharedTransitionScope) {
                                sharedElement(
                                    state =
                                        rememberSharedContentState(key = "back"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = { _, _ ->
                                        tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                                    }
                                )
                            }
                        }.padding(end = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick =
                                dropUnlessResumed {
                                    onClickBack()
                                }
                        ).constrainAs(backRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        },
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(R.string.search_back),
                tint = Color.White
            )
            Text(
                modifier =
                    Modifier
                        .sharedTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope
                        ) { sharedTransitionScope, animatedVisibilityScope ->
                            with(sharedTransitionScope) {
                                sharedBounds(
                                    sharedContentState =
                                        rememberSharedContentState(
                                            key =
                                                SharedElementKey(
                                                    id = movie.id,
                                                    origin = origin,
                                                    type = SharedElementType.TITLE
                                                )
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    resizeMode = ResizeMode.ScaleToBounds(),
                                    boundsTransform = { _, _ ->
                                        tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                                    }
                                )
                            }
                        }.constrainAs(titleRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                text = movie.title,
                style = TheMovieTheme.typography.titleVeryLarge,
                color = TheMovieTheme.colors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(movie.posterPath)
                        .placeholderMemoryCacheKey(imageKey)
                        .memoryCacheKey(imageKey)
                        .build(),
                contentDescription = "Icon",
                clipToBounds = true,
                contentScale = ContentScale.Companion.Crop,
                modifier =
                    Modifier
                        .width(250.dp)
                        .aspectRatio(0.65f)
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
                                sharedBounds(
                                    sharedContentState =
                                        rememberSharedContentState(
                                            key =
                                                SharedElementKey(
                                                    id = movie.id,
                                                    origin = origin,
                                                    type = SharedElementType.POSTER
                                                )
                                        ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    clipInOverlayDuringTransition =
                                        OverlayClip(
                                            RoundedCornerShape(cornerSize?.value ?: 24.dp)
                                        ),
                                    boundsTransform = { initialBounds, targetBounds ->
                                        tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                                    },
                                    resizeMode = ResizeMode.ScaleToBounds(),
                                    enter = EnterTransition.None,
                                    exit = ExitTransition.None
                                )
                            }
                        }.constrainAs(posterRef) {
                            top.linkTo(titleRef.bottom, 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }.clip(TheMovieTheme.shapes.large)
            )
            Icon(
                modifier =
                    Modifier
                        .constrainAs(favoriteRef) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }.clickable {
                            onClickFavorite()
                        },
                imageVector =
                    if (isFavoriteMovie) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                contentDescription = "Favorite",
                tint = TheMovieTheme.colors.error
            )
            Text(
                modifier =
                    Modifier.constrainAs(releaseDateRef) {
                        top.linkTo(posterRef.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = movie.releaseDate,
                style = TheMovieTheme.typography.bodyLarge,
                color = TheMovieTheme.colors.text
            )
            Text(
                modifier =
                    Modifier.constrainAs(overviewRef) {
                        top.linkTo(releaseDateRef.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                text = movie.overview,
                style = TheMovieTheme.typography.bodySmall,
                color = TheMovieTheme.colors.text
            )
        }
    }
}

@Preview
@Composable
fun DetailsScreenContentPreview(
    @PreviewParameter(DetailsPreviewParametersProvider::class)
    movie: Movie
) {
    TheMovieTheme {
        DetailsScreenContent(
            modifier = Modifier.fillMaxSize(),
            isFavoriteMovie = false,
            movie = movie
        )
    }
}
