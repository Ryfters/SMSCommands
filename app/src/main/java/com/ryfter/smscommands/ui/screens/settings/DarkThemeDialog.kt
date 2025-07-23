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
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.RadioItem
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.pop

@Composable
fun DarkThemeDialog(
    backStack: MyNavBackStack,
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
                        backStack.pop()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_theme_light),
                    selected = darkThemeType == 1,
                    onSelected = {
                        viewModel.updateDarkThemeType(1)
                        backStack.pop()
                    }
                )
                RadioItem(
                    text = stringResource(R.string.screen_settings_theme_dark),
                    selected = darkThemeType == 2,
                    onSelected = {
                        viewModel.updateDarkThemeType(2)
                        backStack.pop()
                    }
                )
            }
        },
        onDismissRequest = {
            backStack.pop()
        },
        confirmButton = {}
    )
}