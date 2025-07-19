package com.ryfter.smscommands.permissions

import android.Manifest
import com.ryfter.smscommands.R

class PhonePermission : Permission {
    override val id = "android.permission.CALL_PHONE"

    override val label = R.string.permission_phone

    private val permissions = arrayOf(
        Manifest.permission.CALL_PHONE
    )
}
