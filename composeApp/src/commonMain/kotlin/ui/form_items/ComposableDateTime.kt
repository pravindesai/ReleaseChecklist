package ui.form_items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor

@Composable
fun DateTimeComposable(
    modifier: Modifier = Modifier,
    title: String = "",
    dateTimeString: State<String>,
    isClickable: Boolean = true,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    helpText: String = "",
    content: @Composable() () -> Unit,
    onAddClicked: () -> Unit = {},
    onToolTipClicked: () -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 8.dp, end = 8.dp, bottom = 18.dp
            )
            .background(color = Color.White)
    ) {
        TitleHeader(
            title = title,
            isRequired = isRequired,
            toolTipText = helpText,
            isEnabled = true,
            isClickable = true,
            onToolTipClicked = { onToolTipClicked() }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )

        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(isEnabled and isClickable) {
                    onAddClicked()
                },
            elevation = 0.dp,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(
                width = 1.dp,
                color = colors.MAT_DARK.asColor()
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(
                            start = 0.dp,
                            end = 10.dp,
                            top = 10.dp,
                            bottom = 10.dp
                        )
                        .background(color = colors.MAT_DARK.asColor()),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = dateTimeString.value,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(
                                start = 10.dp
                            )
                    )

                    content()
                }
            }
        }
    }
}