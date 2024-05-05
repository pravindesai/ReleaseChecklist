import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.dashboardscreen.DashboardScreen
import screens.mainscreen.UserSelectionScreen
import screens.splashscreen.SplashScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(screen = SplashScreen(),
            disposeBehavior = NavigatorDisposeBehavior(
                disposeNestedNavigators = true,
                disposeSteps = false
            ),

            onBackPressed = { currentScreen ->
            when(currentScreen){
                is UserSelectionScreen -> false
                is DashboardScreen -> false
                else -> true
            }

        })
    }
}