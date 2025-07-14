package com.ryfter.smscommands.commands.params

import androidx.annotation.StringRes

class FlagParamDefinition(
    @get:StringRes override val name: Int,
    @get:StringRes override val desc: Int,
) : ParamsDefinition()