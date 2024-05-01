import Screen.SplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.MainScreen
import kotlin.time.Duration.Companion.seconds

@Composable
@Preview
fun App() {
    var isSplashShowing by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isSplashShowing){
        if (isSplashShowing){
            coroutineScope.launch {
                delay(3.seconds)
                isSplashShowing = isSplashShowing.not()
            }
        }
    }
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()){
            if (isSplashShowing){
                SplashScreen()
            }else{
                MainScreen()
            }
        }

    }
}