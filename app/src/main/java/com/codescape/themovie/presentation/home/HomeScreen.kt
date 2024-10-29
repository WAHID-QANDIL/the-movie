package com.codescape.themovie.presentation.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.codescape.themovie.R
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.components.MessageCard
import com.codescape.themovie.presentation.home.component.MovieCard
import com.codescape.themovie.presentation.home.preview.HomePreviewParametersProvider
import com.codescape.themovie.presentation.modifier.sharedTransition
import com.codescape.themovie.presentation.shared.SharedElementKey
import com.codescape.themovie.presentation.shared.SharedElementType
import com.codescape.themovie.presentation.theme.TheMovieTheme
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: HomeViewModel,
    onClickSearch: () -> Unit,
    onClickMovie: (Movie, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(
        modifier = modifier,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        uiState = uiState,
        onClickSearch = onClickSearch,
        onClickMovie = onClickMovie
    )
}

// Needed to get current page of carousel (need to open feature request in Google IssueTracker)
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalFoundationApi::class,
    FlowPreview::class,
    ExperimentalMaterialApi::class
)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    uiState: HomeUiState,
    onClickSearch: () -> Unit = {},
    onClickMovie: (Movie, String) -> Unit
) {
    val movies = uiState.movies.collectAsLazyPagingItems()
    var currentItem by remember { mutableIntStateOf(0) }
    var backdropPath by remember { mutableStateOf("") }
    var manualRefresh by remember(movies) { mutableStateOf(false) }
    val isLoading by remember {
        derivedStateOf { !movies.loadState.isIdle && !movies.loadState.hasError }
    }
    val hasError by remember {
        derivedStateOf { movies.loadState.hasError }
    }
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = manualRefresh && isLoading,
            onRefresh = {
                manualRefresh = true
                movies.refresh()
            }
        )
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            manualRefresh = false
        }
    }
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        val backgroundColor = TheMovieTheme.colors.background
        AsyncImage(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(backdropPath)
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
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            stickyHeader(key = 1) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier =
                                Modifier
                                    .padding(
                                        start = 24.dp,
                                        end = 16.dp
                                    ).size(32.dp),
                            imageVector = Icons.Default.Movie,
                            contentDescription = stringResource(R.string.home_search),
                            tint = TheMovieTheme.colors.error
                        )
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = TheMovieTheme.typography.titleVeryLarge,
                            color = TheMovieTheme.colors.text
                        )
                    }
                    Icon(
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 24.dp)
                                .clickable(
                                    onClick =
                                        dropUnlessResumed {
                                            onClickSearch()
                                        }
                                ),
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.home_search),
                        tint = Color.White
                    )
                }
            }
            item(key = 2) {
                Text(
                    modifier =
                        Modifier.padding(
                            top = 24.dp,
                            start = 24.dp,
                            end = 24.dp
                        ),
                    text = "Recent",
                    style = TheMovieTheme.typography.titleVeryLarge,
                    color = TheMovieTheme.colors.text
                )
            }
            if (hasError) {
                item(key = 3) {
                    MessageCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.home_error_title),
                        buttonText = stringResource(R.string.home_error_button),
                        onClick =
                            dropUnlessResumed {
                                movies.retry()
                            }
                    )
                }
            } else {
                item(key = 4) {
                    val carouselState =
                        rememberCarouselState(
                            initialItem = 0,
                            itemCount = { movies.itemCount }
                        )
                    LaunchedEffect(carouselState.pagerState.currentPage) {
                        snapshotFlow { carouselState.pagerState.currentPage }
                            .debounce(300.milliseconds)
                            .collect { page ->
                                if (movies.itemSnapshotList.isNotEmpty()) {
                                    movies.itemSnapshotList[page]?.let { movie ->
                                        backdropPath = movie.backdropPath.orEmpty()
                                    }
                                }
                            }
                    }
                    HorizontalMultiBrowseCarousel(
                        modifier = Modifier.fillMaxWidth(),
                        state = carouselState,
                        preferredItemWidth = 150.dp,
                        itemSpacing = 16.dp,
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) { page ->
                        currentItem = page
                        movies[page]?.let { movie ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                MovieCard(
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .aspectRatio(0.65f)
                                            .clickable(
                                                onClick =
                                                    dropUnlessResumed {
                                                        onClickMovie(movie, "recent")
                                                    }
                                            ).maskClip(TheMovieTheme.shapes.medium)
                                            .animateItem(),
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    origin = "recent",
                                    movie = movie
                                )
                                Text(
                                    text = movie.title,
                                    style = TheMovieTheme.typography.titleLarge,
                                    color = TheMovieTheme.colors.text,
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp)
                                            .maskClip(RectangleShape)
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
                                                                        origin = "recent",
                                                                        type = SharedElementType.TITLE
                                                                    )
                                                            ),
                                                        animatedVisibilityScope = animatedVisibilityScope
                                                    )
                                                }
                                            },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            item(key = 5) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = stringResource(R.string.home_favorites),
                    style = TheMovieTheme.typography.titleVeryLarge,
                    color = TheMovieTheme.colors.text
                )
            }
            if (uiState.favoriteMovies.isEmpty()) {
                item(key = 6) {
                    MessageCard(
                        modifier = Modifier.fillMaxWidth(),
                        title = stringResource(R.string.home_no_favorites_title),
                        buttonText = stringResource(R.string.home_no_favorites_button),
                        onClick =
                            dropUnlessResumed {
                                onClickSearch()
                            }
                    )
                }
            } else {
                item(key = "favorites${uiState.favoriteMovies.size}") {
                    val carouselState =
                        rememberCarouselState(
                            initialItem = 0,
                            itemCount = { uiState.favoriteMovies.size }
                        )
                    HorizontalMultiBrowseCarousel(
                        modifier = Modifier.fillMaxWidth(),
                        state = carouselState,
                        preferredItemWidth = 150.dp,
                        itemSpacing = 16.dp,
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) { page ->
                        if (page in uiState.favoriteMovies.indices) {
                            uiState.favoriteMovies[page].let { movie ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    MovieCard(
                                        modifier =
                                            Modifier
                                                .fillMaxSize()
                                                .aspectRatio(0.65f)
                                                .clickable(
                                                    onClick =
                                                        dropUnlessResumed {
                                                            onClickMovie(movie, "favorites")
                                                        }
                                                ).maskClip(MaterialTheme.shapes.medium)
                                                .animateItem(),
                                        sharedTransitionScope = sharedTransitionScope,
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        origin = "favorites",
                                        movie = movie
                                    )
                                    Text(
                                        text = movie.title,
                                        style = TheMovieTheme.typography.titleLarge,
                                        color = TheMovieTheme.colors.text,
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                                .maskClip(RectangleShape)
                                                .sharedTransition(
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
                                                                            origin = "favorites",
                                                                            type = SharedElementType.TITLE
                                                                        )
                                                                ),
                                                            animatedVisibilityScope = animatedVisibilityScope,
                                                            renderInOverlayDuringTransition = true
                                                        )
                                                    }
                                                },
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = isLoading,
            state = pullRefreshState,
            modifier =
                Modifier
                    .size(48.dp)
                    .align(Alignment.TopCenter),
            backgroundColor = TheMovieTheme.colors.background,
            contentColor = TheMovieTheme.colors.textLink
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun HomeScreenContentPreview(
    @PreviewParameter(HomePreviewParametersProvider::class)
    uiState: HomeUiState
) {
    TheMovieTheme {
        HomeScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onClickMovie = { _, _ -> }
        )
    }
}
