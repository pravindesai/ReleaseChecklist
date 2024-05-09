import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorDisposeBehavior
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.dashboardscreen.DashboardScreen
import screens.mainscreen.UserSelectionScreen
import screens.splashscreen.SplashScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(
            screen = SplashScreen(),
            disposeBehavior = NavigatorDisposeBehavior(
                disposeNestedNavigators = true,
                disposeSteps = false
            ),
            onBackPressed = { currentScreen ->
                when (currentScreen) {
                    is UserSelectionScreen -> false
                    is DashboardScreen -> false
                    else -> true
                }

            }, content = { navigator ->
                SlideTransition(
                    navigator = navigator,
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        visibilityThreshold = IntOffset.VisibilityThreshold
                    )
                )
            })
    }
}