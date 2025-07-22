package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.dialogNavEntry
import com.ryfter.smscommands.ui.screens.history.ClearHistoryDialog
import com.ryfter.smscommands.ui.screens.history.HistoryItemScreen
import com.ryfter.smscommands.ui.screens.history.HistoryScreen

fun history(
    key: Route.History,
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
): NavEntry<Route> {
    return when (key) {
        is Route.History.HiMain -> NavEntry(key) {
            HistoryScreen(backStack, viewModel)
        }

        is Route.History.Item -> NavEntry(key) {
            HistoryItemScreen(backStack, viewModel, key.id)
        }

        is Route.History.ClearDialog -> dialogNavEntry(key) {
            ClearHistoryDialog(backStack, viewModel)
        }
    }
}
