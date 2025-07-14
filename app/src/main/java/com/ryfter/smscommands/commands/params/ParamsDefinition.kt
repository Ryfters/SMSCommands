package com.ryfter.smscommands.commands.params

import androidx.annotation.StringRes

sealed class ParamsDefinition {
    @get:StringRes
    abstract val name: Int

    @get:StringRes
    abstract val desc: Int
}