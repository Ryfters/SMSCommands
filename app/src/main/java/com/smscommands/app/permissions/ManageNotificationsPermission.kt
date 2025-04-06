package com.smscommands.app.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.smscommands.app.BuildConfig
import com.smscommands.app.R

class ManageNotificationsPermission : Permission {
    override val id = "android.permission.MANAGE_NOTIFICATIONS"

    override val label = R.string.permission_manage_notifications
    override val description = R.string.permission_manage_notifications_desc

    override val optional = true

    override fun isGranted(context: Context): Boolean {
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return enabledListeners?.contains(BuildConfig.APPLICATION_ID) == true
    }

    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val context = LocalContext.current
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)

        return {
            context.startActivity(intent)
        }
    }
}