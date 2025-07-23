package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.dialogNavEntry
import com.ryfter.smscommands.ui.screens.home.EditPinDialog
import com.ryfter.smscommands.ui.screens.home.HomeScreen

fun home(key: Route.Home, backStack: MyNavBackStack, viewModel: UiStateViewModel): NavEntry<Route> {
    return when (key) {
        is Route.Home.HoMain -> NavEntry(key) {
            HomeScreen(backStack, viewModel)
        }

        is Route.Home.EditPinDialog -> dialogNavEntry(key) {
            EditPinDialog(backStack, viewModel)
        }
    }
}