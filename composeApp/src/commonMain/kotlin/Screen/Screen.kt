package Screen

import kotlin.jvm.Strictfp

//sealed class Screen() {
//    data object SplashScreen : Screen()
//    data object UserSelectionScreen : Screen()
//    data object LoginScreen : Screen()
//    data object DashboardScreen : Screen()
//
// }


enum class AppScreens(){
    SPLASH_SCREEN,
    USER_SELECTION_SCREEN,
    LOGIN_SCREEN,
    DASHBOARD_SCREEN
}