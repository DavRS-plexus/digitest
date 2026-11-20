package com.plexus.example.libs.androidutils.compose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
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

/**
 * A text a mapper or ViewModel can build without a Context: either literal (it comes from the
 * backend, e.g. a promotion name) or a string resource (the app's own labels, translated).
 * The screen turns it into a String with [asString].
 */
sealed interface UiText {
    data class Dynamic(val value: String) : UiText

    class Resource(@param:StringRes val id: Int, vararg val args: Any) : UiText
}

@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Dynamic -> value
    is UiText.Resource -> stringResource(id, *args)
}
