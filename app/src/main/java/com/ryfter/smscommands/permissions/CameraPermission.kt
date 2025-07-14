package com.ryfter.smscommands.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.ryfter.smscommands.R

class CameraPermission : Permission {
    override val id = "android.permission.CAMERA"

    override val label = R.string.permission_camera

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
    )

    override fun isGranted(context: Context): Boolean {
        return permissions.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result ->
                onResult(result.all { entry -> entry.value })
            }
        )

        return {
            launcher.launch(permissions)
        }
    }
}