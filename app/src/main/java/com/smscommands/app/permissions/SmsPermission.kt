package com.smscommands.app.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.smscommands.app.R


class SmsPermission : Permission {
    override val id = "android.permission.RECEIVE_SMS"

    override val label = R.string.permission_sms
    
    override val description = R.string.permission_sms_desc

    private val permissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS
    )

    override fun isGranted(context: Context): Boolean {
        return permissions.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result -> onResult(result.all { entry -> entry.value }) }
        )
        return {
            launcher.launch(permissions)
        }
    }
}

