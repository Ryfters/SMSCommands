package com.smscommands.app.commands.params

import android.content.Context
import androidx.annotation.StringRes
import com.smscommands.app.R

class ChoiceParamDefinition(
    @get:StringRes override val name: Int,
    @get:StringRes override val desc: Int,
    override val defaultValue: Any? = null,
    override val required: Boolean = false,
    val choices: Map<Int, Any>,
) : OptionParamDefinition() {

    override fun verifyParam(context: Context, param: String): Boolean {
        return choices.any { context.getString(it.key).lowercase() == param.lowercase() }
    }

    override fun parseParam(context: Context, param: String): Any {
        return choices.entries.first {
            context.getString(it.key).lowercase() == param.lowercase()
        }.value
    }

    override fun possibleValues(context: Context): String {
        val possibleValues = choices.keys.joinToString { context.getString(it) }
        return context.getString(R.string.param_choices_possible_values, possibleValues)
    }
}