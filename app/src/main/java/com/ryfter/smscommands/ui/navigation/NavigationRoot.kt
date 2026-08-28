package com.ryfter.smscommands.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.scene.DialogSceneStrategy.Companion.dialog
import androidx.navigation3.ui.NavDisplay
import com.ryfter.smscommands.BuildConfig
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.subgraphs.commands
import com.ryfter.smscommands.ui.navigation.subgraphs.history
import com.ryfter.smscommands.ui.navigation.subgraphs.home
import com.ryfter.smscommands.ui.navigation.subgraphs.lock
import com.ryfter.smscommands.ui.navigation.subgraphs.onboarding
import com.ryfter.smscommands.ui.navigation.subgraphs.perms
import com.ryfter.smscommands.ui.navigation.subgraphs.settings

@Composable
fun NavigationRoot(
    viewModel: UiStateViewModel
) {

    val backStack: MyNavBackStack = remember { mutableStateListOf(Route.Home.HoMain) }

    val signedIn by viewModel.signedIn.collectAsState()
    val requireUnlock by viewModel.requirePin.collectAsState()
    val lastBuildNumber by viewModel.lastBuildNumber.collectAsState()

    if (!signedIn && requireUnlock) backStack.set(Route.Lock.LMain)
    else if (lastBuildNumber == -1) backStack.set(Route.Onboarding.OMain)
    else if (lastBuildNumber < BuildConfig.VERSION_CODE)
        backStack.set(Route.Home.HoMain, Route.Onboarding.UpdateDialog)

    val entryProvider: (Route) -> NavEntry<Route> = {
        when (it) {
            is Route.Home -> home(it, backStack, viewModel)
            is Route.Perms -> perms(it, backStack, viewModel)
            is Route.Commands -> commands(it, backStack, viewModel)
            is Route.History -> history(it, backStack, viewModel)
            is Route.Onboarding -> onboarding(it, backStack, viewModel)
            is Route.Settings -> settings(it, backStack, viewModel)
            is Route.Lock -> lock(it, backStack, viewModel)
        }
    }

    if (backStack.isEmpty()) backStack.navigate(Route.Home.HoMain)

    val animationSpec = tween<IntOffset>(600)
    val animationSpecF = tween<Float>(600)

    NavDisplay(
        backStack = backStack,
        sceneStrategies = listOf(DialogSceneStrategy()),
        entryProvider = entryProvider,
        onBack = { backStack.pop() },
        popTransitionSpec = {
            scaleIn(
                animationSpec = animationSpecF,
                initialScale = 0.8f,
                transformOrigin = TransformOrigin(0.02f, 0.5f),
            ) + fadeIn(animationSpecF) togetherWith scaleOut(
                animationSpec = animationSpecF,
                targetScale = 0.98f,
                transformOrigin = TransformOrigin(1f, 0.5f)
            ) + slideOutOfContainer(
                towards = End,
                animationSpec = animationSpec,
                targetOffset = { it / 2 }
            ) + fadeOut(animationSpecF, targetAlpha = 0.01f)
        },
        predictivePopTransitionSpec = {
            scaleIn(
                animationSpec = animationSpecF,
                initialScale = 0.8f,
                transformOrigin = TransformOrigin(0.02f, 0.5f),
            ) + fadeIn(animationSpecF) togetherWith scaleOut(
                animationSpec = animationSpecF,
                targetScale = 0.98f,
                transformOrigin = TransformOrigin(1f, 0.5f)
            ) + slideOutOfContainer(
                towards = End,
                animationSpec = animationSpec,
                targetOffset = { it / 2 }
            ) + fadeOut(animationSpecF, targetAlpha = 0.01f)
        },
        modifier = Modifier.background(Color.Black)
    )
}

fun dialogNavEntry(
    key: Route,
    metadata: Map<String, Any> = emptyMap(),
    content: @Composable (Route) -> Unit
): NavEntry<Route> = NavEntry(
    key = key,
    metadata = dialog() + metadata,
    content = content
)


