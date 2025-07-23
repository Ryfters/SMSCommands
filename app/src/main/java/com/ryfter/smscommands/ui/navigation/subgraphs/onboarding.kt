package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.dialogNavEntry
import com.ryfter.smscommands.ui.screens.onboarding.ChangelogScreen
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingScreen
import com.ryfter.smscommands.ui.screens.onboarding.UpdateDialog

fun onboarding(
    key: Route.Onboarding,
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
): NavEntry<Route> {
    return when (key) {
        is Route.Onboarding.OMain -> NavEntry(key) {
            OnboardingScreen(backStack, viewModel)
        }
        is Route.Onboarding.UpdateDialog -> dialogNavEntry(key) {
            UpdateDialog(backStack, viewModel)
        }

        is Route.Onboarding.ChangelogScreen -> NavEntry(key) {
            ChangelogScreen(backStack, viewModel)
        }
    }
}

