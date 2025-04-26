package com.smscommands.app.permissions

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.smscommands.app.R


class OverlayPermission : Permission {
    override val id = "android.permission.SYSTEM_ALERT_WINDOW"

    override val label = R.string.permission_overlay

    override val description = R.string.permission_overlay_desc

    override fun isGranted(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val context = LocalContext.current

        val uri = "package:${context.packageName}".toUri()
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = uri // Wont work android11+
        }

        return {
            context.startActivity(intent)
        }
    }
}

