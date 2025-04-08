package com.smscommands.app.permissions

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable


interface Permission {
    val id: String

    @get:StringRes
    val label: Int

    @get:StringRes
    val description: Int?
        get() = null

    val required: Boolean
        get() = false
    val optional: Boolean
        get() = false


    fun isGranted(context: Context): Boolean

    @Composable
    fun request(onResult: (Boolean) -> Unit): () -> Unit

    companion object {
        val ADMIN = AdminPermission()
        val CAMERA = CameraPermission()
        val LOCATION = LocationPermission()
        val MANAGE_NOTIFICATIONS = ManageNotificationsPermission()
        val NOTIFICATION = NotificationPermission()
        val OVERLAY = OverlayPermission()
        val PHONE = PhonePermission()
        val SMS = SmsPermission()

        val LIST = listOf( // Sorted by importance
            SMS,
            NOTIFICATION,
            MANAGE_NOTIFICATIONS,
            OVERLAY,
            ADMIN,
            CAMERA,
            LOCATION,
            PHONE,
        )
    }
}

