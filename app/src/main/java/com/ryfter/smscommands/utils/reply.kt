package com.ryfter.smscommands.utils

import android.content.Context
import android.telephony.SmsManager
import com.ryfter.smscommands.data.SyncPreferences

fun reply(context: Context, message: String, sender: String, id: Long?) {
    val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    val dividedMessage = smsManager.divideMessage(message)
    smsManager.sendMultipartTextMessage(sender, null, dividedMessage, null, null)

    id?.let {
        val syncPreferences = SyncPreferences.getPreferences(context)
        syncPreferences.addToResponse(it, message)
    }
}

