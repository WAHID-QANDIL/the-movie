package com.codescape.themovie.presentation.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.codescape.themovie.R
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.home.component.MovieCard
import com.codescape.themovie.presentation.modifier.sharedTransition
import com.codescape.themovie.presentation.search.preview.SearchPreviewParametersProvider
import com.codescape.themovie.presentation.shared.LocalAnimatedVisibilityScope
import com.codescape.themovie.presentation.shared.LocalSharedTransitionScope
import com.codescape.themovie.presentation.theme.TheMovieTheme
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onClickBack: () -> Unit = {},
    onClickMovie: (Movie, String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SearchScreenContent(
        modifier = modifier,
        uiState = uiState,
        onClickBack = onClickBack,
        onSearch = viewModel::search,
        onClear = viewModel::clear,
        onClickMovie = onClickMovie
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    uiState: SearchUiState,
    onClickBack: () -> Unit = {},
    onSearch: (TextFieldValue) -> Unit = {},
    onClear: () -> Unit = {},
    onClickMovie: (Movie, String) -> Unit
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
        contentPadding =
            PaddingValues(
                top = 8.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(
            key = 0,
            span = { GridItemSpan(maxLineSpan) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                            ),
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.search_back),
                    tint = Color.White
                )
                OutlinedTextField(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                isFocused = it.isFocused
                            },
                    value = uiState.search,
                    onValueChange = onSearch,
                    colors =
                        TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = TheMovieTheme.colors.outline,
                            unfocusedContainerColor = TheMovieTheme.colors.outline,
                            disabledContainerColor = TheMovieTheme.colors.outline,
                            focusedTextColor = TheMovieTheme.colors.text,
                            unfocusedTextColor = TheMovieTheme.colors.text,
                            disabledTextColor = TheMovieTheme.colors.text,
                            cursorColor = TheMovieTheme.colors.text
                        ),
                    shape = RoundedCornerShape(8.dp),
                    leadingIcon = {
                        Icon(
                            modifier =
                                Modifier.sharedTransition(
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
                                ) { sharedTransitionScope, animatedVisibilityScope ->
                                    with(sharedTransitionScope) {
                                        sharedElement(
                                            state =
                                                rememberSharedContentState(key = "search"),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            boundsTransform = { _, _ ->
                                                tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
                                            }
                                        )
                                    }
                                },
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_title),
                            tint = TheMovieTheme.colors.text
                        )
                    },
                    trailingIcon = {
                        Icon(
                            modifier =
                                Modifier.clickable {
                                    onClear()
                                    focusManager.clearFocus()
                                },
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.search_clear),
                            tint =
                                if (uiState.search.text.isNotEmpty()) {
                                    TheMovieTheme.colors.onOutline
                                } else {
                                    TheMovieTheme.colors.text
                                }
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_title),
                            style = TheMovieTheme.typography.bodyMedium,
                            color = TheMovieTheme.colors.textVariant
                        )
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }
        items(
            items = uiState.movies,
            key = { it.id }
        ) { movie ->
            MovieCard(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .aspectRatio(0.65f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick =
                                dropUnlessResumed {
                                    onClickMovie(movie, "recent")
                                }
                        ).clip(TheMovieTheme.shapes.medium)
                        .animateItem(),
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                origin = "recent",
                movie = movie
            )
        }
    }
}


@Preview
@Composable
fun HomeScreenContentPreview(
    @PreviewParameter(SearchPreviewParametersProvider::class)
    uiState: SearchUiState
) {
    TheMovieTheme {
        SearchScreenContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onClickMovie = { _, _ -> }
        )
    }
}
