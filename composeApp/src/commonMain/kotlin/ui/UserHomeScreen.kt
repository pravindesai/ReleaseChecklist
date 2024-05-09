package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import repository.models.data.ObjUser


@Composable
fun UserHomeScreen(user: ObjUser?) {
    val coroutineScope = rememberCoroutineScope()
    val tabNavigator = LocalNavigator.currentOrThrow
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val userState by remember { mutableStateOf(user) }


    //UI
    Column(modifier = Modifier.fillMaxSize()) {

    }


    if (isLoading) {
        AppProgressBar()
    }

    if (showDialog) {
        Box(
            modifier = Modifier.fillMaxSize().clickable {
                showDialog = false
            },
            contentAlignment = Alignment.Center
        ) {
            AppAlertDialog(
                modifier = Modifier.wrapContentSize(),
                showPositiveButton = false,
                dialogTitle = "Alert",
                dialogText = dialogMessage,
                onDismissRequest = {
                    showDialog = false
                },
                onConfirmation = {
                    showDialog = false
                })
        }
    }

}