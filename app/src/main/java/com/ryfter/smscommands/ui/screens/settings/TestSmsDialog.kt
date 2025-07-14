package com.ryfter.smscommands.ui.screens.settings

import android.content.ComponentName
import android.content.Intent
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ryfter.smscommands.R
import com.ryfter.smscommands.receiver.SMSReceiver
import com.ryfter.smscommands.receiver.SMSReceiver.Companion.ACTION_PROCESS_MESSAGE
import com.ryfter.smscommands.receiver.SMSReceiver.Companion.MESSAGE_EXTRA
import com.ryfter.smscommands.receiver.SMSReceiver.Companion.SENDER_EXTRA
import com.ryfter.smscommands.ui.components.MyTextButton

@Composable
fun TestSmsDialog(
    navController: NavController
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var senderInput by rememberSaveable { mutableStateOf("+1 (650) 555-1212") }
    var msgInput by rememberSaveable { mutableStateOf("") }
    var closeTaskOnSend by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        title = { Text(stringResource(R.string.screen_settings_tester_dialog_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = senderInput,
                    onValueChange = { senderInput = it },
                    label = { Text(stringResource(R.string.screen_settings_tester_dialog_sender)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                )
                OutlinedTextField(
                    value = msgInput,
                    onValueChange = { msgInput = it },
                    label = { Text(stringResource(R.string.screen_settings_tester_dialog_message)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Send
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                val intent = Intent(ACTION_PROCESS_MESSAGE).apply {
                                    putExtra(SENDER_EXTRA, senderInput)
                                    putExtra(MESSAGE_EXTRA, msgInput)
                                    component = ComponentName(context, SMSReceiver::class.java)
                                }
                                context.sendBroadcast(intent)
                                if (closeTaskOnSend)
                                    if (activity != null) activity.finishAndRemoveTask()
                                    else Log.e("TestSmsDialog", "Activity is null")
                            }
                        ) {
                            Icon(painterResource(R.drawable.ic_send), contentDescription = null)
                        }
                    }
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.screen_settings_tester_dialog_close_on_send),
                        modifier = Modifier.padding(start =  8.dp)
                    )
                    Checkbox(
                        checked = closeTaskOnSend,
                        onCheckedChange = { closeTaskOnSend = it }
                    )
                }
            }
        },
        confirmButton = {
            MyTextButton(stringResource(R.string.common_close)) { navController.popBackStack() }
        },
        onDismissRequest = {
            navController.popBackStack()
        }
    )
}