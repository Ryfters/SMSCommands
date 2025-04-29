package com.smscommands.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "command_history")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val time: Instant,
    val commandId: String,
    val status: Int,
    val sender: String,
    val trigger: String,
    val messages: List<String>,
)