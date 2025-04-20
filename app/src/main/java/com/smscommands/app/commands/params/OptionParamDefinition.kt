package com.smscommands.app.commands.params

import android.content.Context
import androidx.annotation.StringRes

sealed class OptionParamDefinition : ParamsDefinition() {
    @get:StringRes
    abstract override val desc: Int

    @get:StringRes
    abstract override val name: Int

    open val required: Boolean = false

    open val defaultValue: Any? = null

    abstract fun verifyParam(context: Context, param: String): Boolean

    abstract fun parseParam(context: Context, param: String): Any

    abstract fun possibleValues(context: Context): String
}