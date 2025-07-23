package com.ryfter.smscommands.ui.navigation

sealed class Route {
    sealed class Home : Route() {
        data object HoMain : Home()
        data object EditPinDialog : Home()
    }

    sealed class Perms : Route() {
        data object PMain : Perms()
        data object DeclineWarningDialog : Perms()
        data class Highlight(val highlight: List<String> = emptyList()) : Perms()
    }

    sealed class Commands : Route() {
        data object CMain : Commands()
        data class Item(val id: String) : Commands()
    }

    sealed class History : Route() {
        data object HiMain : History()
        data object ClearDialog : History()
        data class Item(val id: Long) : History()
    }

    sealed class Onboarding : Route() {
        data object OMain : Onboarding()
        data object UpdateDialog : Onboarding()
        data object ChangelogScreen : Onboarding()
    }

    sealed class Lock : Route() {
        data object LMain : Lock()
    }

    sealed class Settings : Route() {
        data object SMain : Settings()
        data object TestSmsDialog : Settings()
        data object DarkThemeDialog : Settings()
        data object DismissNotificationsDialog : Settings()
    }
}

