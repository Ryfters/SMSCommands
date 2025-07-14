package com.ryfter.smscommands.utils

fun <T> Iterable<T>.joinToStringOrNone(
    fallback: String,
    separator: String,
    prefix: String = "\n",
    transform: ((T) -> CharSequence)? = null
): String {
    return if (this.none()) fallback
    else this.joinToString(separator, prefix, transform = transform)
}