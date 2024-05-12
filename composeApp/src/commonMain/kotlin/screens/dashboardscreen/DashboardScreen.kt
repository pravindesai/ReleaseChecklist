package screens.dashboardscreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import colors.asColor
import repository.models.data.IntUser
import screens.dashboardscreen.tabs.HomeTab
import screens.dashboardscreen.tabs.SettingTab
import screens.dashboardscreen.tabs.TabNavigationItem

class DashboardScreen(val user: IntUser) :Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {

        TabNavigator(
            tab = HomeTab,
            content = { tabNavigator ->
                Scaffold(
                    modifier = Modifier.animateContentSize(),
                        content =  {
                            CurrentTab()
                        },
                        bottomBar = {
                            BottomNavigation(
                                modifier = Modifier.animateContentSize(),
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


