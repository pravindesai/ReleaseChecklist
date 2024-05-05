package screens.splashscreen

import colors.YELLOW_LIGHT
import colors.asColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import screens.mainscreen.UserSelectionScreen
import kotlin.time.Duration.Companion.seconds

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isSplashShowing by remember { mutableStateOf(true) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(isSplashShowing){
            if (isSplashShowing){
                coroutineScope.launch {
                    delay(2.seconds)
                    isSplashShowing = isSplashShowing.not()
                }
            }else{
                navigator.push(UserSelectionScreen())
            }
        }
        Box(modifier = Modifier.fillMaxSize().background(color = YELLOW_LIGHT.asColor()))
    }
}
