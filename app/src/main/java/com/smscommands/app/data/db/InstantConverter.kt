package com.smscommands.app.data.db

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    @TypeConverter
    fun fromInstant(instant: Instant): Long {
        return instant.toEpochMilli()
    }

    @TypeConverter
    fun toInstant(timestamp: Long): Instant {
        return Instant.ofEpochMilli(timestamp)
    }
}