package com.smscommands.app.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.smscommands.app.R


class LocationPermission : Permission {
    override val id = "android.permission.ACCESS_BACKGROUND_LOCATION"

    override val label = R.string.permission_location

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    override fun isGranted(context: Context): Boolean {
        return permissions.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
    }


    @Composable
    override fun request(onResult: (Boolean) -> Unit): () -> Unit {

        val backgroundLocationArray = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        var launcher: ActivityResultLauncher<Array<String>>? = null
        launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { result ->
                if (result[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true) {
                    onResult(true)
                } else if (result[Manifest.permission.ACCESS_FINE_LOCATION] == true
                    || result[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                    // Ask for background location cuz you cant get both at the same time
                    launcher?.launch(backgroundLocationArray)
                } else {
                    onResult(false)
                }
            }
        )

        return {
            launcher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }
}

