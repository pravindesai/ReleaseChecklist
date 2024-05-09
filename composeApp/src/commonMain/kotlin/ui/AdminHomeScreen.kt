package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.MAT_DARK
import colors.PRIMARY_ORANGE
import colors.asColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.data.ObjAdmin
import repository.models.data.ObjDocument
import repository.models.data.ObjUser

enum class AdminListViewType {
    Users, Projects, Releases
}

@Composable
fun AdminHomeScreen(admin: ObjAdmin?) {
    val coroutineScope = rememberCoroutineScope()
    val tabNavigator = LocalNavigator.currentOrThrow
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val adminState by remember { mutableStateOf(admin) }

    var selectedListViewType by remember { mutableStateOf(AdminListViewType.Users) }
    var usersList by remember { mutableStateOf(emptyList<ObjUser>()) }
    var projectList by remember { mutableStateOf(emptyList<ObjDocument>()) }
    var releasesList by remember { mutableStateOf(emptyList<ObjDocument>()) }

    LaunchedEffect(selectedListViewType) {
        if ((admin?.fields?.adminid?.stringValue != null)) {
            isLoading = true

            coroutineScope.launch(Dispatchers.IO) {
                when (selectedListViewType) {
                    AdminListViewType.Users -> {
                        val apiResult = CommonRepository.getAllUsersForAdmin(
                            adminState?.fields?.adminid?.stringValue ?: ""
                        )
                        if ((apiResult?.success == true).not()) {
                            isLoading = false
                            dialogMessage = apiResult?.message ?: "FAILED"
                            showDialog = true
                        } else {
                            isLoading = false
                            usersList = apiResult?.data ?: emptyList()
                        }
                    }

                    AdminListViewType.Projects -> {
                        val apiResult = CommonRepository.getAllProjectForAdmin(
                            adminState?.fields?.adminid?.stringValue ?: ""
                        )
                        if ((apiResult?.success == true).not()) {
                            isLoading = false
                            dialogMessage = apiResult?.message ?: "FAILED"
                            showDialog = true
                        } else {
                            isLoading = false
                            projectList = apiResult?.data ?: emptyList()
                        }
                    }

                    AdminListViewType.Releases -> {
                        val apiResult = CommonRepository.getAllProjectForAdmin(
                            adminState?.fields?.adminid?.stringValue ?: ""
                        )
                        if ((apiResult?.success == true).not()) {
                            isLoading = false
                            dialogMessage = apiResult?.message ?: "FAILED"
                            showDialog = true
                        } else {
                            isLoading = false
                            releasesList = apiResult?.data ?: emptyList()
                        }
                    }
                }
            }
        }
    }


    Column {
        //UI
        CustomViewTypeSelectionTab(selectedType = selectedListViewType, onViewTypeChanged = {
            selectedListViewType = it
        })

        when (selectedListViewType) {
            AdminListViewType.Users -> UsersListForAdmin(modifier = Modifier.fillMaxWidth().weight(1f), usersList = usersList)
            AdminListViewType.Projects -> ProjectListForAdmin(modifier = Modifier.fillMaxWidth().weight(1f), usersList = projectList)
            AdminListViewType.Releases -> ProjectListForAdmin(modifier = Modifier.fillMaxWidth().weight(1f), usersList = releasesList)
        }

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





