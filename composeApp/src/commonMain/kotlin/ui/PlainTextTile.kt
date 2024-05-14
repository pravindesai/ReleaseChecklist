package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor

@Composable
fun PlainTextTile(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = colors.MAT_DARK.asColor(),
    onClick: () -> Unit = {}
) {
    Column(modifier = Modifier.clickable {
        onClick()
    }) {
        Row(
            modifier = modifier.fillMaxWidth().wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                color = textColor,
                text = text,
                modifier = Modifier.wrapContentSize(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

}