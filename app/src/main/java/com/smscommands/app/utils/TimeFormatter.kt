package com.smscommands.app.utils

import android.content.Context
import com.smscommands.app.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.TimeZone

fun formatRelativeTime(
    context: Context,
    pastInstant: Instant
): String {

    val userTimeZone = TimeZone.getDefault().toZoneId()
    val now = LocalDateTime.now(userTimeZone)

    val pastDateTime = LocalDateTime.ofInstant(pastInstant, userTimeZone)

    val daysDifference = ChronoUnit.DAYS.between(pastDateTime.toLocalDate(), now.toLocalDate())
    val yearsDifference = ChronoUnit.YEARS.between(pastDateTime.toLocalDate(), now.toLocalDate())

    val timeFormat = when {
        daysDifference == 0L -> context.getString(R.string.timeformat_today_at)
        daysDifference == 1L -> context.getString(R.string.timeformat_yesterday_at)
        daysDifference < 7 -> context.getString(R.string.timeformat_x_days_ago_at, daysDifference.toString())
        yearsDifference == 0L -> context.getString(R.string.timeformat_dd_MM_at)
        else -> context.getString(R.string.timeformat_dd_MM_yy_at)
    }

    val formatter = DateTimeFormatter.ofPattern(timeFormat, Locale.getDefault())
    return pastDateTime.format(formatter)
}


