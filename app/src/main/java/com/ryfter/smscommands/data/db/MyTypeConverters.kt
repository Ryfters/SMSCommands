package com.ryfter.smscommands.data.db

import androidx.room.TypeConverter
import java.time.Instant

class MyTypeConverters {

    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun toInstant(timestamp: Long): Instant = Instant.ofEpochMilli(timestamp)


    @TypeConverter
    fun fromStringList(stringList: List<String>): String =
        stringList.joinToString("//") { it.replace("/", "\\/") }

    @TypeConverter
    fun toStringList(serializedString: String): List<String> =
        serializedString.split("//").map { it.replace("\\/", "/") }.filter { it.isNotEmpty() }
}