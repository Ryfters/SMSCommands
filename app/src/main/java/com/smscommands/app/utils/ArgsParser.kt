package com.smscommands.app.utils

fun getArgumentValue(args: List<String>, prefix: String): String? {
    return args.find { value ->
        value.lowercase().startsWith(prefix.lowercase())
    }?.removePrefix(prefix)
}

fun containsArgument(args: List<String>, argument: String): Boolean {
    return args.any { value ->
        value.lowercase().startsWith(argument.lowercase())
    }
}
