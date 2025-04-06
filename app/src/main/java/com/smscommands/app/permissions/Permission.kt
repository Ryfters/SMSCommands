package com.smscommands.app.permissions

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
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
        val LIST = arrayOf(
            SmsPermission(),
            NotificationPermission(),
            ManageNotificationsPermission(),
            OverlayPermission(),
            AdminPermission(),
            CameraPermission(),
            LocationPermission(),
            PhonePermission(),
        )
    }
}

typealias PermLauncher = ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>?

