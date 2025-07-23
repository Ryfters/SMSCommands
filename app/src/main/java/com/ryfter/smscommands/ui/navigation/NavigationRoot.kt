package com.ryfter.smscommands.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.DialogSceneStrategy
import androidx.navigation3.ui.DialogSceneStrategy.Companion.dialog
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

    NavDisplay(
        backStack = backStack,
        sceneStrategy = DialogSceneStrategy(),
        onBack = { backStack.pop() },
        entryProvider = entryProvider
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


