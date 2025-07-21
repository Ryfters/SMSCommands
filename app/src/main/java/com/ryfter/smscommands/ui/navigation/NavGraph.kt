package com.ryfter.smscommands.ui.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ryfter.smscommands.BuildConfig
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.subgraphs.commands
import com.ryfter.smscommands.ui.navigation.subgraphs.history
import com.ryfter.smscommands.ui.navigation.subgraphs.home
import com.ryfter.smscommands.ui.navigation.subgraphs.onboarding
import com.ryfter.smscommands.ui.navigation.subgraphs.permissions
import com.ryfter.smscommands.ui.navigation.subgraphs.settings

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: UiStateViewModel
) {
    val lastBuildNum by viewModel.lastBuildNumber.collectAsState()

    val currentBuildNum = BuildConfig.VERSION_CODE

    val startDestination =
        if (lastBuildNum == -1) Routes.Onboarding.MAIN // -1 means first launch
        else Routes.Home.MAIN

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {

        home(navController, viewModel)

        history(navController, viewModel)

        permissions(navController, viewModel)

        commands(navController, viewModel)

        onboarding(navController, viewModel)

        settings(navController, viewModel)

    }
}