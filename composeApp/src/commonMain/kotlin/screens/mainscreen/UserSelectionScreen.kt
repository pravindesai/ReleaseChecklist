package screens.mainscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.LIGHT_GRAY
import colors.MAT_WHITE
import colors.asColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.dashboardscreen.DashboardScreen
import screens.loginscreen.LoginScreen
import screens.splashscreen.SplashScreen
import strings.UserType
import ui.PlainTextTile


class UserSelectionScreen : Screen {
    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()

        val adminSectionWeight by remember { mutableFloatStateOf(0.3f) }
        val userSectionWeight by remember { mutableFloatStateOf(0.7f) }
        val navigator = LocalNavigator.currentOrThrow
        var tileOne by remember { mutableStateOf(false) }
        var tileTwo by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            navigator.popUntil {
                (navigator.lastItem == it) or (it is SplashScreen)
            }
            coroutineScope.launch {
                delay(70)
                tileOne = true
                delay(70)
                tileTwo = true
            }
        }

        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

            AnimatedContent(
                modifier = Modifier.weight(adminSectionWeight)
                    ,
                targetState = tileOne,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(200),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                }
            ) { targetState ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(
                        color = if (targetState) colors.YELLOW_LIGHT.asColor() else colors.MAT_WHITE.asColor()
                    ).fillMaxWidth().clickable {
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
            }

            AnimatedContent(
                modifier = Modifier.fillMaxWidth()
                    .weight(userSectionWeight),
                targetState = tileTwo,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(200),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                }
            ) { targetState ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.background(
                        color = if (targetState) colors.PRIMARY_ORANGE_LIGHT.asColor() else colors.MAT_WHITE.asColor()

                    ).clickable {
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
    }

    private fun navigateToLoginAs(navigator: Navigator, userType: UserType) {
        navigator.push(LoginScreen(userType = userType))
    }
}
