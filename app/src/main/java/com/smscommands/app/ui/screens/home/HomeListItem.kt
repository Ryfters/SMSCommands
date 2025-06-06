//package com.smscommands.app.ui.screens.home
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForward
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.smscommands.app.ui.components.MyListItem
//
//@Composable
//fun HomeListItem(
//    headline: String,
//    content: String?,
//    onClick: () -> Unit,
//) {
//    MyListItem(
//        headline,
//        supportingContent = { content?.let { Text(it) } },
//        trailingContent = {
//            IconButton(
//                onClick = onClick,
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                    contentDescription = headline,
//                )
//            }
//        },
//        modifier = Modifier
//            .padding(vertical = 4.dp)
//            .clickable(onClick = onClick),
//    )
//}