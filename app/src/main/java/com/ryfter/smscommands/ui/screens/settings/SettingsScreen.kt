package com.ryfter.smscommands.ui.screens.settings

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.ryfter.smscommands.BuildConfig
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.ui.components.MainScaffold
import com.ryfter.smscommands.ui.components.MyListItem
import com.ryfter.smscommands.ui.components.Subtitle
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.navigate
import com.ryfter.smscommands.ui.navigation.set
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    backStack: MyNavBackStack,
    viewModel: UiStateViewModel,
) {
    MainScaffold(
        backStack = backStack,
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


        Subtitle(stringResource(R.string.screen_settings_subtitle_preferences))
        MyListItem(
            title = stringResource(R.string.screen_settings_dismiss_title),
            content = dismissMessagesContent,
            onClick = {
                backStack.navigate(Route.Settings.DismissNotificationsDialog)
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
                backStack.navigate(Route.History.HiMain)
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
            title = stringResource(R.string.screen_settings_dark_theme),
            content = darkThemeContent,
            onClick = {
                backStack.navigate(Route.Settings.DarkThemeDialog)
            }
        )

        Subtitle(stringResource(R.string.screen_settings_subtitle_other))
        MyListItem(
            title = stringResource(R.string.screen_settings_view_onboarding),
            onClick = {
                backStack.set(Route.Onboarding.OMain)
            }
        )
        MyListItem(
            title = stringResource(R.string.screen_onboarding_changelog_title),
            onClick = {
                backStack.navigate(Route.Onboarding.ChangelogScreen)
            }
        )

        if (BuildConfig.DEBUG) {
            MyListItem(
                title = stringResource(R.string.screen_settings_testsms_title),
                content = stringResource(R.string.screen_settings_testsms_content),
                onClick = {
                    backStack.navigate(Route.Settings.TestSmsDialog)
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
    }
}

