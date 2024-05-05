package screens.dashboardscreen.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import screens.SettingsScreen.SettingsScreen

object SettingTab : Tab {

    @Composable
    override fun Content() {
        SettingsScreen().Content()
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Settings)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Setting",
                    icon = icon
                )
            }
        }
}