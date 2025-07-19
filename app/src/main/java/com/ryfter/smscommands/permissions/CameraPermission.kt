package com.ryfter.smscommands.permissions

import android.Manifest
import com.ryfter.smscommands.R

class CameraPermission : Permission {
    override val id = "android.permission.CAMERA"

    override val label = R.string.permission_camera

    override val permissions = arrayOf(
        Manifest.permission.CAMERA,
    )
}