package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.commands.CommandItemScreen
import com.ryfter.smscommands.ui.screens.commands.CommandsScreen

fun NavGraphBuilder.commands(navController: NavHostController, viewModel: UiStateViewModel) {
    
    composable(Routes.Commands.MAIN) {
        CommandsScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
    
    composable(Routes.Commands.ITEM + "{commandId}", listOf(navArgument("commandId") { type = NavType.StringType })) {
        val commandId = it.arguments?.getString("commandId") ?: ""
        CommandItemScreen(
            navController = navController,
            viewModel = viewModel,
            commandId = commandId
        )
    }
}
