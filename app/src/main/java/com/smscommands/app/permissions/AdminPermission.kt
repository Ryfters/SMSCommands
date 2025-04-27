package com.smscommands.app.permissions

import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.smscommands.app.R
import com.smscommands.app.receiver.AdminReceiver
import com.smscommands.app.receiver.AdminReceiver.Companion.IS_ENABLED_EXTRA
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdminPermission : Permission {
    override val id = "android.permission.DEVICE_ADMIN"

    override val label = R.string.permission_admin

    private var _onResult: (Boolean) -> Unit = {}

    override fun isGranted(context: Context): Boolean {
        val devicePolicyManager = context.getSystemService(DevicePolicyManager::class.java)
        val componentName = ComponentName(context, AdminReceiver::class.java)
        return devicePolicyManager.isAdminActive(componentName)
    }

    private val onEnabledReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            context.unregisterReceiver(this)
            val isEnabled = intent.getBooleanExtra(IS_ENABLED_EXTRA, false)
            _onResult(isEnabled)
        }
    }

    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val context = LocalContext.current

        val filter = IntentFilter(AdminReceiver.ON_ENABLED_INTENT)

        val componentName = ComponentName(context, AdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
        }

        return {
            context.registerReceiver(onEnabledReceiver, filter, Context.RECEIVER_EXPORTED)
            context.startActivity(intent)
            CoroutineScope(Dispatchers.IO).launch {
                delay(60_000)
                try {
                    context.unregisterReceiver(onEnabledReceiver)
                } catch (_: Exception) {}
            }
        }
    }


}