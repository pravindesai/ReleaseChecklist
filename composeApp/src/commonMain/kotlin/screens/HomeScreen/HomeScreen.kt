package screens.HomeScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import repository.CommonRepository
import repository.models.data.ObjAdmin
import repository.models.data.ObjUser
import ui.AdminHomeScreen
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



