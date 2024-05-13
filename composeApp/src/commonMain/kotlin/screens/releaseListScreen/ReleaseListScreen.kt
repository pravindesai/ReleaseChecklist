package screens.releaseListScreen

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.ApiResult
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import ui.AdminListViewType
import ui.AppAlertDialog
import ui.AppProgressBar
import ui.ReleaseItemsList
import ui.ScreenHeaderWithTitle


class ReleaseListScreen(
    val viewType: AdminListViewType,
    val objUser: ObjUser? = null,
    val objDocument: ObjDocument? = null
) : Screen {

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val tabNavigator = LocalNavigator.currentOrThrow
        var isLoading by remember { mutableStateOf(false) }

        var releasesListState by remember { mutableStateOf(listOf<ObjDocument>()) }
        val viewTypeState by remember { mutableStateOf(viewType) }
        var showFailureDialog by remember { mutableStateOf(Pair(false, "")) }

        LaunchedEffect(key1 = viewTypeState) {
            when (viewTypeState) {
                AdminListViewType.Users -> {
                    coroutineScope.launch(Dispatchers.IO) {
                        isLoading = true
                        val result = objUser?.let { getAllReleasesForUser(objUser.fields?.userId?.stringValue?:objUser.fields?.userid?.stringValue?:"") }

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

        //UI

        Column(modifier = Modifier.fillMaxSize()) {
            ScreenHeaderWithTitle(title = when (viewTypeState) {
                AdminListViewType.Users -> "Releases by ${objUser?.fields?.userId?.stringValue ?: objUser?.fields?.userid?.stringValue ?: ""}"
                AdminListViewType.Projects -> "Release for ${objDocument?.fields?.projectName?.stringValue ?: ""}"
                AdminListViewType.Releases -> "Recent Releases"
            }, onBackClick = {
                tabNavigator.pop()
            })

            Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
                ReleaseItemsList(
                    listOfReleases = releasesListState,
                    onCardClick = { doc ->

                    },
                    onDeleteClick = { doc ->

                    }
                )
            }

        }

        if (isLoading) {
            AppProgressBar()
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

    }

}

suspend fun getAllReleasesForProject(objDocument: ObjDocument): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForProject(objDocument)
}


suspend fun getAllReleasesForAdmin(): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForAdmin()
}

suspend fun getAllReleasesForUser(userId:String): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForUser(userId)
}


