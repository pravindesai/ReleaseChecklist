package ui

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
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

    TopAppBar(
        title = {
            Text(
                title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                textAlign = TextAlign.Start,
            )
        },
        modifier = appBarModifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
        }
    )
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