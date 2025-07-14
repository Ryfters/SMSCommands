package com.ryfter.smscommands.commands.params

import android.content.Context
import androidx.annotation.StringRes

class CustomOptionParamDefinition(
    @get:StringRes override val name: Int,
    @get:StringRes override val desc: Int,
    override val defaultValue: Any? = null,
    override val required: Boolean = false,
    private val _verifyParam: (Context, String) -> Boolean = { _, _ -> true },
    private val _parseParam: (Context, String) -> Any = { _, param -> param },
    private val _possibleValues: (Context) -> String = { _ -> "" },
) : OptionParamDefinition() {
    override fun verifyParam(context: Context, param: String): Boolean =
        _verifyParam(context, param)

    override fun parseParam(context: Context, param: String): Any = _parseParam(context, param)

    override fun possibleValues(context: Context): String = _possibleValues(context)

}