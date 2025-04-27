package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.home.EditPinDialog
import com.smscommands.app.ui.screens.home.HomeScreen

fun NavGraphBuilder.home(navController: NavHostController, viewModel: UiStateViewModel) {
    composable(Routes.Home.MAIN) {
        HomeScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
    dialog(Routes.Home.EDIT_PIN_DIALOG) {
        EditPinDialog(
            navController = navController,
            viewModel = viewModel
        )
    }
}