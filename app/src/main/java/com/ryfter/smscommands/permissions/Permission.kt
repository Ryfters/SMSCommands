package com.ryfter.smscommands.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable


@Suppress("SameReturnValue")
interface Permission {
    val id: String

    @get:StringRes
    val label: Int

    @get:StringRes
    val description: Int?
        get() = null

    val permissions: Array<String>
        get() = arrayOf()

    fun isGranted(context: Context): Boolean {
        return permissions.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result ->
                onResult(result.all { entry -> entry.value })
            }
        )
        return {
            launcher.launch(permissions)
        }
    }

    companion object {
        val ADMIN = AdminPermission()
        val CAMERA = CameraPermission()
        val LOCATION = LocationPermission()
        val MANAGE_NOTIFICATIONS = ManageNotificationsPermission()
        val OVERLAY = OverlayPermission()
        val PHONE = PhonePermission()
        val SMS = SmsPermission()

        val BASE = listOf(
            SMS,
        )

        val OPTIONAL = listOf(
            MANAGE_NOTIFICATIONS,
        )

        val COMMANDS = listOf(
            OVERLAY,
            ADMIN,
            CAMERA,
            LOCATION,
            PHONE,
        )

        val ALL = BASE + OPTIONAL + COMMANDS
    }
}

