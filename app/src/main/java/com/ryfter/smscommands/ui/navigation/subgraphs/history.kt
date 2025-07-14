package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.history.ClearHistoryDialog
import com.ryfter.smscommands.ui.screens.history.HistoryItemScreen
import com.ryfter.smscommands.ui.screens.history.HistoryScreen

fun NavGraphBuilder.history(navController: NavHostController, viewModel: UiStateViewModel) {
    
    composable(Routes.History.MAIN) {
        HistoryScreen(
            navController = navController,
            viewModel = viewModel
        )
    }

    composable(
        Routes.History.ITEM_DIALOG + "{itemId}",
        listOf(navArgument("itemId") { type = NavType.IntType })
    ) {
        val itemId = it.arguments?.getInt("itemId")?.toLong() ?: 0L
        HistoryItemScreen(
            navController = navController,
            viewModel = viewModel,
            itemId = itemId
        )
    }

    dialog(Routes.History.CLEAR_DIALOG) {
        ClearHistoryDialog(
            navController = navController,
            viewModel = viewModel,
        )
    }
}
