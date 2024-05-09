package screens.dashboardscreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import repository.models.data.IntUser
import screens.dashboardscreen.tabs.HomeTab
import screens.dashboardscreen.tabs.SettingTab
import screens.dashboardscreen.tabs.TabNavigationItem
import strings.UserType
import kotlin.time.Duration.Companion.seconds

class DashboardScreen(val user: IntUser) :Screen {

    @Composable
    override fun Content() {

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


