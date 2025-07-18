package com.ryfter.smscommands.commands.params

import android.content.Context
import androidx.annotation.StringRes

class ChoiceParamDefinition(
    @get:StringRes override val name: Int,
    @get:StringRes override val desc: Int,
    override val defaultValue: Any? = null,
    override val required: Boolean = false,
    val choices: Map<Any, Int>,
) : OptionParamDefinition() {

    override fun verifyParam(context: Context, param: String): Boolean {
        return choices.any { context.getString(it.value).lowercase() == param.lowercase() }
    }

    override fun parseParam(context: Context, param: String): Any {
        return choices.entries.first {
            context.getString(it.value).lowercase() == param.lowercase()
        }.key
    }

    override fun possibleValues(context: Context): String {
        return choices.values.joinToString { context.getString(it) }
    }
}