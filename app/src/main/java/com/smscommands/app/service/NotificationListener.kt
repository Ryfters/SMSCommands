package com.smscommands.app.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.smscommands.app.data.SyncPreferences

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notification = sbn?.notification ?: return

        val extras = notification.extras
        val text = extras?.getCharSequence(Notification.EXTRA_TEXT).toString()

        val commandRegex = "!!\\d{0,8}/[\\s\\S]*".toRegex()
        if (!text.matches(commandRegex)) return

        val syncPrefs = SyncPreferences.getPreferences(applicationContext)
        val dismissMessages = syncPrefs.readDismissNotificationType()

        if (dismissMessages == 0) return

        if (dismissMessages == 2) {
            val matchingButtons = notification.actions?.filter { action ->
                action.remoteInputs?.size == null
            }
            // Not matching by text for other languages
            // Only run if theres one valid button, else we might press a wrong button
            if (matchingButtons?.size == 1) {
                matchingButtons[0].actionIntent.send()
            }
        }

        cancelNotification(sbn.key)

    }
}