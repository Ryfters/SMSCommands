package com.smscommands.app.commands.params

import android.content.Context
import androidx.annotation.StringRes
import com.smscommands.app.R

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
        val possibleValues = choices.values.joinToString { context.getString(it) }
        return context.getString(R.string.param_choices_possible_values, possibleValues)
    }
}