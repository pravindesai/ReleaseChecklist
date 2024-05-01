package screens.dashboardscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import strings.UserType

class DashboardScreen(userType: UserType) :Screen {
    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize().background(color = Color.Cyan))
    }
}