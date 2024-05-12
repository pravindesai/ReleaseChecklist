package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.ApiResult
import repository.models.data.ObjAdmin
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import screens.releaseListScreen.ReleaseListScreen

enum class AdminListViewType {
    Users, Projects, Releases
}

var selectedViewTypeGlobal = AdminListViewType.Users

@Composable
fun AdminHomeScreen(admin: ObjAdmin?) {
    val coroutineScope = rememberCoroutineScope()
    val tabNavigator = LocalNavigator.currentOrThrow
    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val adminState by remember { mutableStateOf(admin) }

    var selectedListViewType by remember { mutableStateOf(selectedViewTypeGlobal) }
    var usersList by remember { mutableStateOf(emptyList<ObjUser>()) }
    var projectList by remember { mutableStateOf(emptyList<ObjDocument>()) }
    var releasesList by remember { mutableStateOf(emptyList<ObjDocument>()) }

    var deleteUser by remember { mutableStateOf<Pair<Boolean, ObjUser?>>(Pair(false, null)) }
    var deleteProject by remember { mutableStateOf<Pair<Boolean, ObjDocument?>>(Pair(false, null)) }
    var deleteRelease by remember { mutableStateOf<Pair<Boolean, ObjDocument?>>(Pair(false, null)) }

    var showDeleteDialog by remember { mutableStateOf(Pair(false, "")) }

    LaunchedEffect(key1 = deleteUser, block = {
        if (deleteUser.first) {
            showDeleteDialog = Pair(true, "Are you sure you want to delete this user ?")
        }
    })


    LaunchedEffect(key1 = deleteProject, block = {
        if (deleteProject.first) {
            showDeleteDialog = Pair(true, "Are you sure you want to delete this project ?")
        }
    })


    LaunchedEffect(key1 = deleteRelease, block = {
        if (deleteRelease.first) {
            showDeleteDialog = Pair(true, "Are you sure you want to delete this release ?")
        }
    })

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
        CustomViewTypeSelectionTab(selectedType = selectedListViewType,
            onViewTypeChanged = {
                selectedViewTypeGlobal = it
                selectedListViewType = it
        })

        when (selectedListViewType) {
            AdminListViewType.Users -> UsersListForAdmin(
                modifier = Modifier.fillMaxWidth().weight(1f).animateContentSize(),
                usersList = usersList,
                onUserSelected = {
                    tabNavigator.parent?.push(ReleaseListScreen(
                        viewType = AdminListViewType.Users,
                        objUser = it
                    ))
                },
                onUserDelete = { deleteUser = Pair(true, it) })

            AdminListViewType.Projects -> ProjectListForAdmin(
                modifier = Modifier.fillMaxWidth().weight(1f).animateContentSize(),
                usersList = projectList,
                onDocumentSelected = {
                    tabNavigator.parent?.push(ReleaseListScreen(
                        viewType = AdminListViewType.Projects,
                        objDocument = it
                    ))
                },
                onDocumentDelete = { deleteProject = Pair(true, it) })

            AdminListViewType.Releases -> ProjectListForAdmin(
                modifier = Modifier.fillMaxWidth().weight(1f).animateContentSize(),
                usersList = releasesList,
                onDocumentSelected = {
                    tabNavigator.parent?.push(ReleaseListScreen(
                        viewType = AdminListViewType.Releases,
                        objDocument = it
                    ))
                },
                onDocumentDelete = { deleteRelease = Pair(true, it) })
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
    if (showDeleteDialog.first) {
        Box(
            modifier = Modifier.fillMaxSize().clickable {
                showDeleteDialog = showDeleteDialog.copy(first = false)
                deleteUser = deleteUser.copy(first = false)
                deleteProject = deleteProject.copy(first = false)
                deleteRelease = deleteRelease.copy(first = false)
            },
            contentAlignment = Alignment.Center
        ) {
            AppAlertDialog(
                modifier = Modifier.wrapContentSize(),
                dialogTitle = "Alert",
                dialogText = showDeleteDialog.second,
                onDismissRequest = {
                    showDeleteDialog = showDeleteDialog.copy(first = false)
                    deleteUser = deleteUser.copy(first = false)
                    deleteProject = deleteProject.copy(first = false)
                    deleteRelease = deleteRelease.copy(first = false)
                },
                onConfirmation = {
                    showDeleteDialog = showDeleteDialog.copy(first = false)

                    when {
                        deleteUser.first -> {
                            deleteUser = deleteUser.copy(first = false)

                            coroutineScope.launch(Dispatchers.IO) {
                                isLoading = true
                                val result = deleteUser.second?.let { deleteUser(it) }

                                if (result?.success == true) {
                                    usersList = usersList.filterNot { it == deleteUser.second }
                                    dialogMessage = "User deleted successfully."
                                    showDialog = true
                                } else {
                                    dialogMessage = result?.message ?: "FAILED"
                                }

                                showDialog = true
                                deleteUser = Pair(false, null)
                                isLoading = false
                            }

                        }

                        deleteProject.first -> {
                            deleteProject = deleteProject.copy(first = false)

                            coroutineScope.launch(Dispatchers.IO) {
                                isLoading = true
                                val result = deleteProject.second?.let { deleteProject(it) }

                                if (result?.success == true) {
                                    projectList =
                                        projectList.filterNot { it == deleteProject.second }
                                    dialogMessage = "Project deleted successfully."
                                    showDialog = true
                                } else {
                                    dialogMessage = result?.message ?: "FAILED"
                                }

                                showDialog = true
                                deleteUser = Pair(false, null)
                                isLoading = false
                            }
                        }

                        deleteRelease.first -> {
                            deleteRelease = deleteRelease.copy(first = false)

                            coroutineScope.launch(Dispatchers.IO) {
                                isLoading = true
                                val result = deleteRelease.second?.let { deleteRelease(it) }

                                if (result?.success == true) {
                                    releasesList =
                                        releasesList.filterNot { it == deleteRelease.second }
                                    dialogMessage = "Project deleted successfully."
                                    showDialog = true
                                } else {
                                    dialogMessage = result?.message ?: "FAILED"
                                }

                                showDialog = true
                                deleteUser = Pair(false, null)
                                isLoading = false
                            }

                        }
                    }
                })
        }
    }

}


suspend fun deleteUser(user: ObjUser): ApiResult<ObjUser> {
    return CommonRepository.deleteUser(user)
}

suspend fun deleteProject(document: ObjDocument): ApiResult<ObjDocument> {
    return CommonRepository.deleteProject(document)
}

suspend fun deleteRelease(document: ObjDocument): ApiResult<ObjDocument> {
    return CommonRepository.deleteRelease(document)
}






