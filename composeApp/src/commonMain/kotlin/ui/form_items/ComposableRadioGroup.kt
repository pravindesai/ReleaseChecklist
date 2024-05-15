package ui.form_items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ComposableRadioGroup(
    modifier: Modifier = Modifier,
    title: String = "",
    isClickable: Boolean = true,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    helpText: String = "",
    defaultSelectedIndex: Int? = null,
    rbList: List<String> = emptyList(),
    onRbgTouched: () -> Unit = {},
    onRadioButtonChecked: (item: String) -> Unit = {},
    onTooltipClicked: () -> Unit = {}
) {

    var selectedItem by remember {
        mutableStateOf(defaultSelectedIndex?.let { rbList.getOrNull(it) } ?: "")
    }

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
            isEnabled = isEnabled,
            isClickable = isClickable,
            onToolTipClicked = { onTooltipClicked() }
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp))
        rbList.forEachIndexed { index, currentText ->
            ComposeRadioButton(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                radioText = currentText,
                isSelected = selectedItem == currentText,
                isClickable = isClickable,
                isEnabled = isEnabled,
                onRadioButtonChecked = { item ->
                    if (selectedItem!=item){
                        selectedItem = item
                        onRadioButtonChecked(selectedItem)
                        onRbgTouched()
                    }
                }
            )
            if (index!=rbList.size-1){
                DottedLine(step = 10.dp,
                    padding = PaddingValues(top = 3.dp, bottom = 3.dp))
            }

        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp))
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeRadioButton(
    modifier: Modifier = Modifier,
    radioText: String,
    isSelected: Boolean = false,
    isClickable: Boolean = true,
    isEnabled: Boolean = true,
    onRadioButtonChecked: (item: String) -> Unit = {}
) {
    Row(modifier = modifier) {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            RadioButton(
                modifier = Modifier,
                enabled = isEnabled and isClickable,
                selected = isSelected, onClick = {
                    onRadioButtonChecked(radioText)
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.Black,
                    unselectedColor = Color.Black
                )
            )

            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 5.dp)
                    .clickable(isClickable and isEnabled, onClick = {
                        onRadioButtonChecked(radioText)
                    }),
                text = radioText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,

                    )
            )
        }
    }
}

