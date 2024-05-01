package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppBar(
    navigateUp: () -> Unit = {},
    canNavigateBack: Boolean = false,
    title: String
) {
    val appBarModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()

    Row(
        modifier =
        appBarModifier
            .background(color = colors.MAT_DARK.asColor()).padding(
                vertical = 5.dp,
                horizontal = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
        }

        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(
                    if (canNavigateBack.not()) 15.dp else 0.dp
                )
                .fillMaxWidth()
                .wrapContentHeight(),
            color = Color.White,
            textAlign = if (canNavigateBack) TextAlign.Start else TextAlign.Center,
        )
    }

//    TopAppBar(
//        title = {
//            Text(
//                title,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight(align = Alignment.CenterVertically),
//                textAlign = TextAlign.Start,
//            )
//        },
//        modifier = appBarModifier,
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(onClick = navigateUp) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = ""
//                    )
//                }
//            }
//        }
//    )
}

@Preview
@Composable
fun AppBarPreview() {
    AppBar(
        navigateUp = {},
        canNavigateBack = true,
        title = "Home"
    )
}