package com.codescape.themovie.presentation.modifier

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier

/**
 *  Applies the given [sharedTransition] to the content of the layout, if both [sharedTransitionScope] and [animatedVisibilityScope] are provided.
 *  If either of them is null, the modifier is not applied.
 *
 *  @param sharedTransitionScope The [SharedTransitionScope] that provides information about the shared transition.
 *  @param animatedVisibilityScope The [AnimatedVisibilityScope] that provides information about the animated content.
 *  @param sharedTransition The modifier that applies the shared transition.
 *
 *  @return The modifier with the shared transition applied, or the original modifier if either [sharedTransitionScope] or [animatedVisibilityScope] is null.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
inline fun Modifier.sharedTransition(
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    sharedTransition: Modifier.(SharedTransitionScope, AnimatedVisibilityScope) -> Modifier
): Modifier =
    if (sharedTransitionScope != null && animatedVisibilityScope != null) {
        sharedTransition(sharedTransitionScope, animatedVisibilityScope)
    } else {
        this
    }
