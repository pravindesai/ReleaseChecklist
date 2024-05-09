package screens.SettingsScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import colors.MAT_DARK
import colors.asColor
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.data.ObjAdmin
import screens.dashboardscreen.DashboardScreen
import screens.mainscreen.UserSelectionScreen
import ui.AppProgressBar

class SettingsScreen : Screen {

    @Composable
    operator fun invoke() = Content()

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val tabNavigator = LocalNavigator.currentOrThrow
        var isLoading by remember { mutableStateOf(false) }
        val currentUser by remember { mutableStateOf(CommonRepository.getLoggedInUser()) }
        val isAdmin by remember {
            derivedStateOf {
                currentUser is ObjAdmin
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(10),
                onClick = {

                    coroutineScope.launch {
                        isLoading = true
                        val isSignedOut = signOut()
                        if (isSignedOut){
                            tabNavigator.parent?.replaceAll(UserSelectionScreen())
                        }
                        isLoading = false
                    }

                },
                modifier = Modifier.fillMaxWidth().padding(10.dp).wrapContentHeight(),
                border = BorderStroke(1.dp, color = MAT_DARK.asColor()),
                content = {
                    Text(
                        color = MAT_DARK.asColor(),
                        text = "Sign Out",
                        modifier = Modifier.wrapContentSize(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            Spacer(modifier = Modifier.height(50.dp))

        }


        if (isLoading){
            AppProgressBar()
        }

    }

    private suspend fun signOut():Boolean{
        return CommonRepository.signOut()
    }

}