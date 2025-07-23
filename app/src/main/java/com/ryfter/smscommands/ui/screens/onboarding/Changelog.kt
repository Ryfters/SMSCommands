package com.ryfter.smscommands.ui.screens.onboarding

val Changelog = mapOf(
    5 to ChangelogItem(
        versionName = "0.4.0",
        changes = listOf("New changelog (this)", "Extra content added in some commands"),
        date = "23/07/25"
    ),
    4 to ChangelogItem(
        versionName = "0.3.0",
        changes = listOf(
            "Improved onboarding",
            "Command separator is a space (instead of backslash)"
        ),
        date = "20/07/25"
    ),
    3 to ChangelogItem(
        versionName = "0.2.0",
        changes = listOf("Capture command now sends back the pictures taken through MMS"),
        date = "19/07/25"
    )
)

data class ChangelogItem(
    val versionName: String,
    val changes: List<String> = emptyList(),
    val date: String
)