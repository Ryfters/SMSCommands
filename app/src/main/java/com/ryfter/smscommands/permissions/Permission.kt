package com.ryfter.smscommands.permissions

import android.content.Context
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

    fun isGranted(context: Context): Boolean

    @Composable
    fun request(onResult: (Boolean) -> Unit): () -> Unit

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

