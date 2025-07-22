package com.ryfter.smscommands.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.navigation.NavigationRoot
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
            NavigationRoot(
                viewModel = viewModel
            )
        }
    }
}

