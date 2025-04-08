package com.smscommands.app.utils

import com.smscommands.app.R
import com.smscommands.app.receiver.CommandStatus.DISABLED_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_PIN
import com.smscommands.app.receiver.CommandStatus.MISSING_PERMISSIONS
import com.smscommands.app.receiver.CommandStatus.SUCCESS

fun getStatusRes(status: String): Int {
    return Status[status] ?: R.string.screen_history_item_status_unknown
}

val Status = mapOf(
    SUCCESS to R.string.screen_history_item_status_success,
    INVALID_PIN to R.string.screen_history_item_invalid_pin,
    INVALID_COMMAND to R.string.screen_history_item_status_invalid_command,
    DISABLED_COMMAND to R.string.screen_history_item_status_disabled_command,
    MISSING_PERMISSIONS to R.string.screen_history_item_status_missing_permissions,
)