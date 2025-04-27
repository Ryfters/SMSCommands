package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.perms.DeclineWarningDialog
import com.smscommands.app.ui.screens.perms.PermsScreen

fun NavGraphBuilder.permissions(navController: NavHostController, viewModel: UiStateViewModel) {
    
    composable(Routes.Perms.MAIN) {
        PermsScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
    
    dialog(Routes.Perms.DECLINE_WARNING_DIALOG) {
        DeclineWarningDialog(
            navController = navController
        )
    }
}