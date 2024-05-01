package screens.dashboardscreen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import colors.asColor
import screens.dashboardscreen.tabs.HomeTab
import screens.dashboardscreen.tabs.SettingTab
import screens.dashboardscreen.tabs.TabNavigationItem
import strings.UserType
import ui.AppBar

class DashboardScreen(userType: UserType) :Screen {
    @Composable
    override fun Content() {
        TabNavigator(
            tab = HomeTab,
            content = { tabNavigator ->
                    Scaffold(
                        content =  {
                            CurrentTab()
                        },
                        bottomBar = {
                            BottomNavigation(
                                backgroundColor = colors.MAT_DARK.asColor()
                            ) {
                                TabNavigationItem(HomeTab)
                                TabNavigationItem(SettingTab)
                            }
                        }
                    )
            }
        )
    }
}


