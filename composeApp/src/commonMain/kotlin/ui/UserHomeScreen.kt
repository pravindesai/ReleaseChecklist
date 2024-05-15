package ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.asColor
import repository.CommonRepository
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
    ReleasesList(
        viewType = AdminListViewType.Users,
        objUser = CommonRepository.getLoggedInUser() as? ObjUser,
        showBackButton = false,
        defaultClosed = true
    )

    Box(modifier = Modifier.padding(bottom = 70.dp, end = 15.dp).fillMaxSize(),
        contentAlignment = Alignment.BottomEnd) {
        Icon(
            modifier = Modifier.clickable {

            }.size(80.dp).padding(0.dp),
            imageVector = Icons.Filled.AddCircle,
            contentDescription = null,
            tint = colors.FLOATING_BTN_COLOR.asColor()
        )
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