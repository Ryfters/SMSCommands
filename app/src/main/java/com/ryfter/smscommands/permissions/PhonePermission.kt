package com.ryfter.smscommands.permissions

import android.Manifest
import com.ryfter.smscommands.R

class PhonePermission : Permission {
    override val id = "android.permission.CALL_PHONE"

    override val label = R.string.permission_phone

    override val permissions = arrayOf(
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.READ_PHONE_STATE,
    )
}
