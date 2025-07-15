package com.ryfter.smscommands.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.provider.Telephony.Sms.Intents.SMS_RECEIVED_ACTION
import com.ryfter.smscommands.BuildConfig

class SMSReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null) return

        val sender: String
        val msgBody: String
        when (intent?.action) {
            SMS_RECEIVED_ACTION -> {
                val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                sender = messages[0]?.originatingAddress.toString()
                msgBody = messages[0].messageBody
            }

            ACTION_PROCESS_MESSAGE -> {
                sender = intent.getStringExtra(SENDER_EXTRA) ?: ""
                msgBody = intent.getStringExtra(MESSAGE_EXTRA) ?: ""
            }

            else -> return
        }

        processMessage(context, sender, msgBody)

    }

    companion object {
        const val ACTION_PROCESS_MESSAGE = "${BuildConfig.APPLICATION_ID}.ACTION_PROCESS_MESSAGE"
        const val SENDER_EXTRA = "sender"
        const val MESSAGE_EXTRA = "message"
    }
}