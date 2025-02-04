package com.codescape.themovie.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.details.DetailsScreen
import com.codescape.themovie.presentation.details.DetailsViewModel
import com.codescape.themovie.presentation.home.HomeScreen
import com.codescape.themovie.presentation.home.HomeViewModel
import com.codescape.themovie.presentation.navigation.navtype.MovieType
import com.codescape.themovie.presentation.search.SearchScreen
import com.codescape.themovie.presentation.search.SearchViewModel
import com.codescape.themovie.presentation.theme.TheMovieTheme
import kotlinx.coroutines.FlowPreview
import kotlin.reflect.typeOf

@OptIn(ExperimentalSharedTransitionApi::class, FlowPreview::class)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    SharedTransitionLayout {
        val navController = rememberNavController()
        Scaffold(modifier = modifier) { paddingValues ->
            NavHost(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(TheMovieTheme.colors.background)
                        .padding(
                            PaddingValues(
                                top = paddingValues.calculateTopPadding(),
                                bottom = paddingValues.calculateBottomPadding()
                            )
                        ),
                navController = navController,
                startDestination = Screen.Home,
                contentAlignment = Alignment.Companion.Center,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            ),
                        initialOffset = { x -> -x }
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            ),
                        targetOffset = { x -> x }
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            ),
                        initialOffset = { x -> x }
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec =
                            tween(
                                durationMillis = 500,
                                easing = FastOutSlowInEasing
                            ),
                        targetOffset = { x -> -x }
                    )
                }
            ) {
                composable<Screen.Home> {
                    val viewModel = hiltViewModel<HomeViewModel>()
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        viewModel = viewModel,
                        onClickSearch = {
                            navController.navigate(Screen.Search)
                        },
                        onClickMovie = { movie, origin ->
                            navController.navigate(Screen.Details(movie = movie, origin = origin))
                        }
                    )
                }
                composable<Screen.Search> {
                    val viewModel = hiltViewModel<SearchViewModel>()
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        viewModel = viewModel,
                        onClickBack = {
                            navController.navigateUp()
                        },
                        onClickMovie = { movie, origin ->
                            navController.navigate(Screen.Details(movie = movie, origin))
                        }
                    )
                }
                composable<Screen.Details>(
                    typeMap =
                        mapOf(
                            typeOf<Movie>() to NavType.MovieType,
                            typeOf<String>() to NavType.StringType
                        )
                ) { backStackEntry ->
                    val movie = backStackEntry.toRoute<Screen.Details>().movie
                    val origin = backStackEntry.toRoute<Screen.Details>().origin
                    val viewModel = hiltViewModel<DetailsViewModel>()
                    DetailsScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        movie = movie,
                        origin = origin,
                        viewModel = viewModel,
                        onClickBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}
