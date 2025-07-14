package com.ryfter.smscommands.ui.navigation

object Routes {

    object Home {
        const val MAIN = "home_main"
        const val EDIT_PIN_DIALOG = "home_edit_pin_dialog"
    }

    object Perms {
        const val MAIN = "perms_main"
        const val DECLINE_WARNING_DIALOG = "perms_decline_warning_dialog"
    }

    object Commands {
        const val MAIN = "commands_main"
        const val ITEM = "commands_item/"
    }

    object History {
        const val MAIN = "history_main"
        const val ITEM_DIALOG = "history_item/"
        const val CLEAR_DIALOG = "history_clear_dialog"
    }

    object Onboarding {
        const val MAIN = "onboarding_intro"
    }

    object Settings {
        const val MAIN = "settings_main"
        const val TEST_SMS_DIALOG = "settings_test_sms_dialog"
        const val DARK_THEME_DIALOG = "settings_dark_theme_dialog"
        const val DISMISS_NOTIFICATIONS_DIALOG = "settings_dismiss_notifications_dialog"
    }
}