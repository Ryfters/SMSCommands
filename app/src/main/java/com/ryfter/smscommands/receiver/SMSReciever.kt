package com.ryfter.smscommands.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SMSReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) return

        val sender: String
        val msgBody: String
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            sender = messages[0]?.originatingAddress.toString()
            msgBody = messages[0].messageBody
        } else if (intent?.action == ACTION_PROCESS_MESSAGE) {
            sender = intent.getStringExtra(SENDER_EXTRA) ?: ""
            msgBody = intent.getStringExtra(MESSAGE_EXTRA) ?: ""
        } else return

        processMessage(context, sender, msgBody)

    }

    companion object {
        const val ACTION_PROCESS_MESSAGE = "com.ryfter.smscommands.ACTION_PROCESS_MESSAGE"
        const val SENDER_EXTRA = "sender"
        const val MESSAGE_EXTRA = "message"
    }
}