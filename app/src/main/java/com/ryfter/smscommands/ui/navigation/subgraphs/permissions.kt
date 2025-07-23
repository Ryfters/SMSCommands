package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.dialogNavEntry
import com.ryfter.smscommands.ui.screens.perms.DeclineWarningDialog
import com.ryfter.smscommands.ui.screens.perms.PermsScreen

fun perms(
    key: Route.Perms,
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
): NavEntry<Route> {
    return when (key) {
        is Route.Perms.PMain -> NavEntry(key) {
            PermsScreen(backStack, viewModel)
        }

        is Route.Perms.DeclineWarningDialog -> dialogNavEntry(key) {
            DeclineWarningDialog(backStack, viewModel)
        }

        is Route.Perms.Highlight -> NavEntry(key) {
            PermsScreen(backStack, viewModel, key.highlight)
        }
    }
}
