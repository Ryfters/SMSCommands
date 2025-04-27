
package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.commands.CommandItemScreen
import com.smscommands.app.ui.screens.commands.CommandsScreen

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
