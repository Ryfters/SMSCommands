package com.ryfter.smscommands.commands.params

import android.content.Context
import androidx.annotation.StringRes
import com.ryfter.smscommands.R

class IntParamDefinition(
    @get:StringRes override val name: Int,
    @get:StringRes override val desc: Int,
    override val defaultValue: Int? = null,
    override val required: Boolean = false,
    val min: Int = 0,
    val max: Int = 9999,
) : OptionParamDefinition() {
    override fun verifyParam(context: Context, param: String): Boolean {
        val value = param.toIntOrNull() ?: return false
        if (value <= min) return false
        if (value >= max) return false
        return true
    }

    override fun parseParam(context: Context, param: String): Any {
        return param.toInt()
    }

    override fun possibleValues(context: Context): String {
        return context.getString(R.string.param_int_possible_values, min, max)
    }
}