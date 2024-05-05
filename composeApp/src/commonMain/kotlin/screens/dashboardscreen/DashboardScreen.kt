package screens.dashboardscreen

import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import colors.asColor
import repository.CommonRepository
import screens.dashboardscreen.tabs.HomeTab
import screens.dashboardscreen.tabs.SettingTab
import screens.dashboardscreen.tabs.TabNavigationItem
import strings.UserType

class DashboardScreen(val userType: UserType) :Screen {

    @Composable
    override fun Content() {
        CommonRepository.setCurrentUserType(userType)

        TabNavigator(
            tab = HomeTab,
            disposeNestedNavigators = true,
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


