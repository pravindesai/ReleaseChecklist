package screens.dashboardscreen.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import colors.PRIMARY_ORANGE
import colors.PRIMARY_ORANGE_LIGHT
import colors.asColor

@Composable
fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = tab.options.icon!!,
                    tint = if (tabNavigator.current.key == tab.key) PRIMARY_ORANGE.asColor() else Color.LightGray,
                    contentDescription = tab.options.title
                )
                Text(
                    tab.options.title,
                    color = if (tabNavigator.current.key == tab.key) PRIMARY_ORANGE.asColor() else Color.LightGray
                )
            }
        }
    )
}