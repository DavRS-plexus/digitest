package com.plexus.example.libs.androidutils.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Runs the callbacks on the lifecycle events of the current [LocalLifecycleOwner] (inside a
 * Navigation Compose graph, the screen's back stack entry).
 *
 * - A new observer receives the events the lifecycle already went through, so `onCreate`,
 *   `onStart` and `onResume` run as soon as it's attached, not only when the screen is created.
 * - `onDestroy` never arrives inside a NavHost: the observer is removed in `onDispose` before the
 *   entry is destroyed. To do something when leaving the screen, use `DisposableEffect`'s `onDispose`.
 */
@Composable
fun ObserveLifecycleEvents(
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    // The observer lives as long as the lifecycleOwner: without this it would keep calling the
    // lambdas of the first composition even if the caller passes new ones
    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDestroy by rememberUpdatedState(onDestroy)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate?.invoke()
                Lifecycle.Event.ON_START -> currentOnStart?.invoke()
                Lifecycle.Event.ON_RESUME -> currentOnResume?.invoke()
                Lifecycle.Event.ON_PAUSE -> currentOnPause?.invoke()
                Lifecycle.Event.ON_STOP -> currentOnStop?.invoke()
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy?.invoke()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
