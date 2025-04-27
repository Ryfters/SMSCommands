package com.smscommands.app.ui.screens.settings

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.smscommands.app.BuildConfig
import com.smscommands.app.R
import com.smscommands.app.data.UiStateViewModel
import com.smscommands.app.ui.components.MainScaffold
import com.smscommands.app.ui.components.MyListItem
import com.smscommands.app.ui.components.Subtitle
import com.smscommands.app.ui.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: UiStateViewModel,
) {
    MainScaffold(
        navController = navController,
        title = stringResource(R.string.screen_settings_title),
        showUpButton = true,
    ) {

        val context = LocalContext.current

        val hideMessagesNotifications by viewModel.dismissNotificationType.collectAsState()
        val dismissMessagesContent = when (hideMessagesNotifications) {
            0 -> stringResource(R.string.screen_settings_dismiss_disable)
            1 -> stringResource(R.string.screen_settings_dismiss_only)
            else -> stringResource(R.string.screen_settings_dismiss_mark_as_read)
        }

        val requirePin by viewModel.requirePin.collectAsState()

        val historyEnabled by viewModel.historyEnabled.collectAsState()

        val dynamicColorsEnabled by viewModel.dynamicColorsEnabled.collectAsState()

        val darkThemeType by viewModel.darkThemeType.collectAsState()
        val darkThemeContent = when (darkThemeType) {
            0 -> stringResource(R.string.screen_settings_theme_sys)
            1 -> stringResource(R.string.screen_settings_theme_light)
            else -> stringResource(R.string.screen_settings_theme_dark)
        }


        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        val buildType = BuildConfig.BUILD_TYPE
        val buildDate = BuildConfig.BUILD_DATE
        val formattedBuildDate = SimpleDateFormat(
            stringResource(R.string.timeformat_dd_MM_yy),
            Locale.getDefault()).format(Date(buildDate)
        )
        val versionContent = stringResource(
            R.string.screen_settings_version_info_content,
            versionName,
            versionCode,
            buildType,
            formattedBuildDate
        )


        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            Subtitle(stringResource(R.string.screen_settings_subtitle_preferences))
            MyListItem(
                title = stringResource(R.string.screen_settings_dismiss_title),
                content = dismissMessagesContent,
                onClick = {
                    navController.navigate(Routes.Settings.DISMISS_NOTIFICATIONS_DIALOG)
                }
            )
            MyListItem(
                title = stringResource(R.string.screen_settings_require_pin_title),
                content = stringResource(R.string.screen_settings_require_pin_content),
                action = {
                    Switch(
                        checked = requirePin,
                        onCheckedChange = { value ->
                            viewModel.updateSignedIn(true)
                            viewModel.updateRequirePin(value)
                        }
                    )
                }
            )
            MyListItem(
                title = stringResource(R.string.screen_settings_history_title),
                content = stringResource(R.string.screen_settings_history_content),
                onClick = {
                    navController.navigate(Routes.History.MAIN) {
                        popUpTo(Routes.Home.MAIN)
                    }
                },
                separator = true,
                action = {
                    Switch(
                        checked = historyEnabled,
                        onCheckedChange = { value ->
                            viewModel.clearHistory()
                            viewModel.updateHistoryEnabled(value)
                        }
                    )
                }
            )

            Subtitle(stringResource(R.string.screen_settings_subtitle_appearance))
            MyListItem(
                title = stringResource(R.string.screen_settings_dynamic_color_title),
                content = stringResource(R.string.screen_settings_dynamic_color_content),
                action = {
                    Switch(
                        checked = dynamicColorsEnabled,
                        onCheckedChange = { value ->
                            viewModel.updateDynamicColorsEnabled(value)
                        }
                    )
                },
            )
            MyListItem(
                title = stringResource(R.string.screen_settings_dark_theme_title),
                content = darkThemeContent,
                onClick = {
                    navController.navigate(Routes.Settings.DARK_THEME_DIALOG)
                }
            )

            Subtitle(stringResource(R.string.screen_settings_subtitle_other))
            MyListItem(
                title = stringResource(R.string.screen_settings_view_onboarding),
                onClick = {
                    navController.navigate(Routes.Onboarding.MAIN) {
                        popUpTo(Routes.Home.MAIN)
                    }
                }
            )

            if (BuildConfig.DEBUG) {
                MyListItem(
                    title = stringResource(R.string.screen_settings_testsms_title),
                    content = stringResource(R.string.screen_settings_testsms_content),
                    onClick = {
                        navController.navigate(Routes.Settings.TEST_SMS_DIALOG)
                    }
                )
            }

            MyListItem(
                title = stringResource(R.string.screen_settings_source_code_title),
                content = stringResource(R.string.screen_settings_source_code_content),
                onClick = {
                    val uri = context.getString(R.string.url_github).toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                action = {
                    Icon(
                        painter = painterResource(R.drawable.ic_open_in),
                        contentDescription = null
                    )
                }
            )
            MyListItem(
                title = stringResource(R.string.screen_settings_update_title),
                content = stringResource(R.string.screen_settings_update_content),
                onClick = {
                    val uri = context.getString(R.string.url_github_releases).toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
                action = {
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = null
                    )
                }
            )
            MyListItem(
                title = stringResource(R.string.screen_settings_version_title),
                content = versionContent,
            )

            Spacer(Modifier.padding(WindowInsets.navigationBars.asPaddingValues()))
        }
    }
}

