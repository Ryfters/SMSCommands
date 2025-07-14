package com.ryfter.smscommands.ui.navigation.subgraphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.Routes
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingScreen

fun NavGraphBuilder.onboarding(navController: NavHostController, viewModel: UiStateViewModel) {

    composable(Routes.Onboarding.MAIN) {
        OnboardingScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}

