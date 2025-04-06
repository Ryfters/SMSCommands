package com.smscommands.app.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import com.smscommands.app.BuildConfig
import com.smscommands.app.receiver.AdminReceiver.Companion.IS_ENABLED_EXTRA
import com.smscommands.app.receiver.AdminReceiver.Companion.ON_ENABLED_INTENT

class AdminReceiver : DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        broadcastAdminEnabled(context, true)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        broadcastAdminEnabled(context, false)
    }

    companion object {
        const val ON_ENABLED_INTENT = "${BuildConfig.APPLICATION_ID}.ON_ADMIN_ENABLED"
        const val IS_ENABLED_EXTRA = "is_enabled"
    }
}

fun broadcastAdminEnabled(context: Context, value: Boolean) {
    val intent = Intent(ON_ENABLED_INTENT)
    intent.putExtra(IS_ENABLED_EXTRA, value)
    context.sendBroadcast(intent)
}