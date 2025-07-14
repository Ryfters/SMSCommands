package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.home.EditPinDialog
import com.ryfter.smscommands.ui.screens.home.HomeScreen

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