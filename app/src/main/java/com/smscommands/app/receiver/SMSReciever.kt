package com.smscommands.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SMSReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action != "android.provider.Telephony.SMS_RECEIVED" || context == null) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val sender = messages[0]?.originatingAddress.toString()
        val msgBody = messages[0].messageBody

        processMessage(context, sender, msgBody)

    }
}