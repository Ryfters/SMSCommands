package com.smscommands.app.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.Routes
import com.smscommands.app.ui.screens.onboarding.OnboardingScreen

fun NavGraphBuilder.onboarding(navController: NavHostController, viewModel: UiStateViewModel) {

    composable(Routes.Onboarding.MAIN) {
        OnboardingScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

