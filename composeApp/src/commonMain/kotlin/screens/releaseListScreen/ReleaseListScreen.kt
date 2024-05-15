package screens.releaseListScreen

import androidx.compose.foundation.Image
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
import coil3.compose.AsyncImage
import colors.asColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import repository.CommonRepository
import repository.models.ApiResult
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import ui.AdminListViewType
import ui.AppAlertDialog
import ui.AppProgressBar
import ui.ReleaseItemsList
import ui.ReleasesList
import ui.ScreenHeaderWithTitle


class ReleaseListScreen(
    val viewType: AdminListViewType,
    val objUser: ObjUser? = null,
    val objDocument: ObjDocument? = null
) : Screen {

    @Composable
    override fun Content() {
        ReleasesList(
            viewType = viewType, objUser = objUser, objDocument = objDocument
        )
    }
}

suspend fun getAllReleasesForProject(objDocument: ObjDocument): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForProject(objDocument)
}


suspend fun getAllReleasesForAdmin(): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForAdmin()
}

suspend fun getAllReleasesForUser(userId: String): ApiResult<List<ObjDocument>> {
    return CommonRepository.getAllReleasesForUser(userId)
}


