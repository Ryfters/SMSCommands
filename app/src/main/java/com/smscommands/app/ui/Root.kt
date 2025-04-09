package com.smscommands.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.navigation.NavGraph
import com.smscommands.app.ui.theme.SMSCommandsTheme

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
        NavGraph(
            navController = rememberNavController(),
            viewModel = viewModel
        )
    }
}

