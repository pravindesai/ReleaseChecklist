package screens.HomeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import repository.CommonRepository
import repository.models.data.ObjAdmin
import repository.models.data.ObjUser
import ui.AdminHomeScreen
import ui.AppAlertDialog
import ui.AppProgressBar
import ui.UserHomeScreen

class HomeScreen : Screen {
    @Composable
    operator fun invoke() = Content()

    @Composable
    override fun Content() {
        val currentUser by remember { mutableStateOf(CommonRepository.getLoggedInUser()) }
        val isAdmin by remember {
            derivedStateOf {
                currentUser is ObjAdmin
            }
        }
        val admin by remember {
            derivedStateOf { currentUser as? ObjAdmin }
        }
        val user by remember {
            derivedStateOf { currentUser as? ObjUser }
        }

        if (isAdmin) {
            AdminHomeScreen(admin)
        } else {
            UserHomeScreen(user)
        }

    }
}



