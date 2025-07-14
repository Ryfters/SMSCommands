package com.ryfter.smscommands.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.NavGraph
import com.ryfter.smscommands.ui.screens.lock.LockScreen
import com.ryfter.smscommands.ui.theme.SMSCommandsTheme

@Composable
fun Root(
    viewModel: UiStateViewModel
) {
    val dynamicColors by viewModel.dynamicColorsEnabled.collectAsState()

    val darkThemeValue by viewModel.darkThemeType.collectAsState()
    val darkTheme = when (darkThemeValue) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    SMSCommandsTheme(
        dynamicColor = dynamicColors,
        darkTheme = darkTheme
    ) {
        Box {
            NavGraph(
                navController = rememberNavController(),
                viewModel = viewModel
            )

            LockScreen(viewModel)
        }
    }
}

