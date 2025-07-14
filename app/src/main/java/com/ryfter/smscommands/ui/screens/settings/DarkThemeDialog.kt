package com.ryfter.smscommands.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.RadioItem

@Composable
fun DarkThemeDialog(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    val darkThemeType by viewModel.darkThemeType.collectAsState()

    AlertDialog(
        title = {
            Text(stringResource(R.string.screen_settings_dark_theme))
        },
        text = {
            Column(
                modifier = Modifier.selectableGroup()
            ) {
                RadioItem(
                    text = stringResource(R.string.screen_settings_theme_sys),
                    selected = darkThemeType == 0,
                    onSelected = {
                        viewModel.updateDarkThemeType(0)
                        navController.popBackStack()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_theme_light),
                    selected = darkThemeType == 1,
                    onSelected = {
                        viewModel.updateDarkThemeType(1)
                        navController.popBackStack()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_theme_dark),
                    selected = darkThemeType == 2,
                    onSelected = {
                        viewModel.updateDarkThemeType(2)
                        navController.popBackStack()
                    }
                )
            }
        },
        onDismissRequest = {
            navController.popBackStack()
        },
        confirmButton = {}
    )
}