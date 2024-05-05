package screens.mainscreen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.asColor
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.dashboardscreen.DashboardScreen
import screens.loginscreen.LoginScreen
import screens.splashscreen.SplashScreen
import strings.UserType


class UserSelectionScreen : Screen {
    @Composable
    override fun Content() {
        val adminSectionWeight by remember { mutableFloatStateOf(0.3f) }
        val userSectionWeight by remember { mutableFloatStateOf(0.7f) }
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit){
            navigator.popUntil {
                (navigator.lastItem == it) or (it is SplashScreen)
            }
        }

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().animateContentSize(
                    animationSpec = tween(durationMillis = 50000, easing = LinearEasing)
                ).weight(adminSectionWeight)
                    .background(color = colors.YELLOW_LIGHT.asColor()).clickable {
//                        adminSectionWeight = 1f
//                        userSectionWeight = 0.001f
                        navigateToLoginAs(
                            navigator = navigator,
                            userType = UserType.ADMIN
                        )
                    }
            ) {
                Text(
                    text = strings.login_as_admin,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = colors.MAT_DARK.asColor(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                    )
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth().animateContentSize(
                    animationSpec = tween(durationMillis = 50000, easing = LinearEasing)
                    )
                    .weight(userSectionWeight)
                    .background(color = colors.PRIMARY_ORANGE_LIGHT.asColor()).clickable {
//                        adminSectionWeight = 0.001f
//                        userSectionWeight = 1f
                        navigateToLoginAs(
                            navigator = navigator,
                            userType = UserType.USER
                        )
                    }
            ) {
                Text(
                    text = strings.login_as_user,
                    modifier = Modifier,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = colors.MAT_DARK.asColor(),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                    )
                )
            }

        }
    }

    private fun navigateToLoginAs(navigator: Navigator, userType: UserType){
        navigator.push(LoginScreen(userType = userType))
    }
}
