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
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import repository.models.data.ObjDocument
import kotlin.time.Duration.Companion.seconds

@Composable
fun ReleasesTabContainer(modifier: Modifier = Modifier,
                         releasesList:List<ObjDocument>) {
    var releasesListState by remember { mutableStateOf(releasesList) }
    var deleteReleaseDialog by remember { mutableStateOf(Pair<Boolean, ObjDocument?>(false, null)) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(releasesList){
        releasesListState = releasesList
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = modifier.fillMaxWidth()) {
            ReleaseItemsList(
                listOfReleases = releasesListState,
                isDeleteAllowed = true,
                onCardClick = { doc ->

                },
                onDeleteClick = { doc ->
                    deleteReleaseDialog = Pair(true, doc)
                }
            )
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

}

