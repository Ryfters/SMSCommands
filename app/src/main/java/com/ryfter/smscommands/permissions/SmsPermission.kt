package com.ryfter.smscommands.permissions

import android.Manifest
import com.ryfter.smscommands.R


class SmsPermission : Permission {
    override val id = "android.permission.RECEIVE_SMS"

    override val label = R.string.permission_sms
    
    override val description = R.string.permission_sms_desc

    override val permissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS
    )
}

