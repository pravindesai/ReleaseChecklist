package ui

import androidx.compose.foundation.background
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
import coil3.compose.AsyncImage
import colors.PLAIN_WHITE
import colors.asColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.CommonRepository.getAllReleasesForAdmin
import repository.CommonRepository.getAllReleasesForProject
import repository.CommonRepository.getAllReleasesForUser
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReleasesList(
    viewType: AdminListViewType,
    objUser: ObjUser? = null,
    objDocument: ObjDocument? = null,
    showBackButton: Boolean = true,
    defaultClosed:Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()
    val tabNavigator = LocalNavigator.currentOrThrow
    var isLoading by remember { mutableStateOf(false) }

    var releasesListState by remember { mutableStateOf(listOf<ObjDocument>()) }
    val viewTypeState by remember { mutableStateOf(viewType) }
    var showFailureDialog by remember { mutableStateOf(Pair(false, "")) }
    var deleteReleaseDialog by remember { mutableStateOf(Pair<Boolean, ObjDocument?>(false, null)) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(key1 = viewTypeState) {
        when (viewTypeState) {
            AdminListViewType.Users -> {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = objUser?.let {
                        getAllReleasesForUser(
                            objUser.fields?.userId?.stringValue
                                ?: objUser.fields?.userid?.stringValue ?: ""
                        )
                    }

                    if (result?.success == true) {
                        val list = result.data ?: emptyList()
                        releasesListState = list
                    } else {
                        releasesListState = emptyList()
                        showFailureDialog = Pair(true, result?.message ?: "FAILED")
                    }
                    isLoading = false
                }
            }

            AdminListViewType.Projects -> {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = objDocument?.let { getAllReleasesForProject(objDocument = it) }

                    if (result?.success == true) {
                        val list = result.data ?: emptyList()
                        releasesListState = list
                    } else {
                        releasesListState = emptyList()
                        showFailureDialog = Pair(true, result?.message ?: "FAILED")
                    }
                    isLoading = false
                }
            }

            AdminListViewType.Releases -> {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = objDocument?.let { getAllReleasesForAdmin() }

                    if (result?.success == true) {
                        val list = result.data ?: emptyList()
                        releasesListState = list
                    } else {
                        releasesListState = emptyList()
                        showFailureDialog = Pair(true, result?.message ?: "FAILED")
                    }
                    isLoading = false
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeaderWithTitle(
            showBackButton = showBackButton,
            title = when (viewTypeState) {
                AdminListViewType.Users -> "Releases by ${objUser?.fields?.userId?.stringValue ?: objUser?.fields?.userid?.stringValue ?: ""}"
                AdminListViewType.Projects -> "Release for ${objDocument?.fields?.projectName?.stringValue ?: ""}"
                AdminListViewType.Releases -> "Recent Releases"
            }, onBackClick = {
                tabNavigator.pop()
            })

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {

            if (releasesListState.isNotEmpty() or isLoading) {
                ReleaseItemsList(
                    listOfReleases = releasesListState,
                    isDeleteAllowed = (CommonRepository.isCurrentUserAdmin()),
                    defaultClosed = defaultClosed,
                    onCardClick = { doc ->

                    },
                    onDeleteClick = { doc ->
                        deleteReleaseDialog = Pair(true, doc)
                    }
                )
            } else {

                Box(
                    modifier = Modifier.fillMaxSize().background(PLAIN_WHITE.asColor()),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = CommonRepository.NO_DATA_FOUND_PLACEHOLDER,
                        contentDescription = null
                    )

                }


            }


        }

    }

    if (showFailureDialog.first) {
        Box(
            modifier = Modifier.fillMaxSize().clickable {
                showFailureDialog = Pair(false, "")
            },
            contentAlignment = Alignment.Center
        ) {
            AppAlertDialog(
                modifier = Modifier.wrapContentSize(),
                dialogTitle = "Alert",
                showNegativeButton = true,
                showPositiveButton = false,
                dialogText = showFailureDialog.second,
                onDismissRequest = {
                    showFailureDialog = Pair(false, "")
                    tabNavigator.pop()
                },
                onConfirmation = {
                    showFailureDialog = Pair(false, "")
                })
        }
    }

    if (deleteReleaseDialog.first){
        Box(
            modifier = Modifier.fillMaxSize().clickable {
                deleteReleaseDialog = Pair(false, null)
            },
            contentAlignment = Alignment.Center
        ) {
            AppAlertDialog(
                modifier = Modifier.wrapContentSize(),
                showPositiveButton = true,
                showNegativeButton = true,
                positiveButtonText = "Yes, Delete it.",
                negativeButtonText = "No",
                dialogTitle = "Alert",
                dialogText = "Are you sure you want to delete ${deleteReleaseDialog.second?.fields?.releaseId?.stringValue} ?",
                onDismissRequest = {
                    deleteReleaseDialog = Pair(false, null)
                },
                onConfirmation = {
                    isLoading = true
                    coroutineScope.launch {

                        val result = deleteReleaseDialog.second?.let { deleteRelease(it) }

                        if (result?.success == true) {
                            releasesListState =  releasesListState.filterNot { it == deleteReleaseDialog.second }
                            dialogMessage = "Release deleted successfully."
                            showDialog = true
                        } else {
                            dialogMessage = result?.message ?: "Failed to delete release.\nPlease try again"
                            showDialog = true
                        }
                        deleteReleaseDialog = Pair(false, null)
                        isLoading = false
                    }
                })
        }
    }

    if (isLoading){
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
