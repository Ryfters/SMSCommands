package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.perms.DeclineWarningDialog
import com.ryfter.smscommands.ui.screens.perms.PermsScreen

fun NavGraphBuilder.permissions(navController: NavHostController, viewModel: UiStateViewModel) {

    composable(
        Routes.Perms.MAIN + "{permissionIds}",
        listOf(navArgument("permissionIds") { type = NavType.StringType })
    ) {
        val permissionIds = (it.arguments?.getString("permissionIds") ?: "").split(", ")

        PermsScreen(
            navController = navController,
            viewModel = viewModel,
            permissionIds = permissionIds
        )
    }
    
    dialog(Routes.Perms.DECLINE_WARNING_DIALOG) {
        DeclineWarningDialog(
            navController = navController
        )
    }
}