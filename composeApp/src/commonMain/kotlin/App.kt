import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.dashboardscreen.DashboardScreen
import screens.mainscreen.MainScreen
import screens.splashscreen.SplashScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = SplashScreen(), onBackPressed = { currentScreen ->
            when(currentScreen){
                is MainScreen -> false
                is DashboardScreen -> false
                else -> true
            }

        })
    }
}