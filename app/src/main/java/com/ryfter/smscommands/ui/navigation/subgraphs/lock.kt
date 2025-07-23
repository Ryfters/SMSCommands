package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation3.runtime.NavEntry
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.screens.lock.LockScreen

fun lock(key: Route.Lock, backStack: MyNavBackStack, viewModel: UiStateViewModel): NavEntry<Route> {
    return when (key) {
        is Route.Lock.LMain -> NavEntry(key) {
            LockScreen(backStack, viewModel)
        }
    }
}