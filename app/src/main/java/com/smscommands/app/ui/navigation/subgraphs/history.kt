package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.history.ClearHistoryDialog
import com.smscommands.app.ui.screens.history.HistoryItemDialog
import com.smscommands.app.ui.screens.history.HistoryScreen

fun NavGraphBuilder.history(navController: NavHostController, viewModel: UiStateViewModel) {
    
    composable(Routes.History.MAIN) {
        HistoryScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
    
    dialog(Routes.History.ITEM_DIALOG + "{itemId}", listOf(navArgument("itemId") { type = NavType.IntType })) {
        val itemId = it.arguments?.getInt("itemId")?.toLong() ?: 0L
        HistoryItemDialog(
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
