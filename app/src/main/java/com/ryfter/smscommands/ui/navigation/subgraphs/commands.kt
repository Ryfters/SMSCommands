package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.screens.commands.CommandItemScreen
import com.ryfter.smscommands.ui.screens.commands.CommandsScreen

fun commands(
    key: Route.Commands,
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel
): NavEntry<Route> {
    return when (key) {
        is Route.Commands.CMain -> NavEntry(key) {
            CommandsScreen(backStack, viewModel)
        }

        is Route.Commands.Item -> NavEntry(key) {
            CommandItemScreen(backStack, viewModel, key.id)
        }
    }
}