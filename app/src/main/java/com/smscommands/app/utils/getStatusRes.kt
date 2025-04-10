package com.smscommands.app.utils

import com.smscommands.app.R
import com.smscommands.app.receiver.CommandStatus.DISABLED_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_ARGS
import com.smscommands.app.receiver.CommandStatus.INVALID_COMMAND
import com.smscommands.app.receiver.CommandStatus.INVALID_PARAM
import com.smscommands.app.receiver.CommandStatus.INVALID_PIN
import com.smscommands.app.receiver.CommandStatus.MISSING_PARAM
import com.smscommands.app.receiver.CommandStatus.MISSING_PERMISSIONS
import com.smscommands.app.receiver.CommandStatus.SUCCESS

fun getStatusRes(status: String): Int {
    return Status[status] ?: R.string.screen_history_item_status_unknown
}

val Status = mapOf(
    DISABLED_COMMAND to R.string.screen_history_item_status_disabled_command,
    INVALID_ARGS to R.string.screen_history_item_status_invalid_args,
    INVALID_COMMAND to R.string.screen_history_item_status_invalid_command,
    INVALID_PARAM to R.string.screen_history_item_statuse_invalid_param,
    INVALID_PIN to R.string.screen_history_item_invalid_pin,
    MISSING_PARAM to R.string.screen_history_item_status_missing_param,
    MISSING_PERMISSIONS to R.string.screen_history_item_status_missing_permissions,
    SUCCESS to R.string.screen_history_item_status_success,
)