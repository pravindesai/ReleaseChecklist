package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import colors.MAT_DARK
import colors.PRIMARY_ORANGE
import colors.PRIMARY_ORANGE_LIGHT
import colors.asColor

@Composable
fun AppProgressBar(){
    Box(
        modifier = Modifier.fillMaxSize().clickable {  },
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            trackColor = PRIMARY_ORANGE.asColor(),
            color = MAT_DARK.asColor(),

            )
    }
}