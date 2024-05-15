package ui.form_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumTouchTargetEnforcement
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CheckBoxList(
    modifier: Modifier = Modifier,
    title: String? = "",
    isRequired: Boolean = false,
    isClickable: Boolean = true,
    isEnabled: Boolean = true,
    helpText: String = "",
    chBxTitleIdSelectedTripleList:List<Triple<String?,Int, Boolean>>,
    onCheckboxSelected:(List<Int>) -> Unit = {_->},
    isSingleLine:Boolean = false,
    onToolTipClicked: () -> Unit = {},
){

    var checkBoxs = remember { mutableStateListOf<Triple<String?,Int, Boolean>>() }

    LaunchedEffect(key1 = Unit, block ={
        checkBoxs.addAll(chBxTitleIdSelectedTripleList)
    } )

    Column(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(
            start = 8.dp, end = 8.dp, bottom = 18.dp
        )
        .background(color = Color.White)) {
        TitleHeader(
            title = title?:"",
            isRequired = isRequired,
            toolTipText = helpText?:"",
            isEnabled = true,
            isClickable = true,
            onToolTipClicked = { onToolTipClicked() }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)


        )
        Column(modifier = Modifier.fillMaxWidth()) {
                checkBoxs.forEachIndexed { bxIndex, (bxTitle,bxCode,isChecked)->
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth().padding(
                                PaddingValues(top = 7.dp, bottom = 7.dp)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
                            Checkbox(
                                modifier = Modifier.padding(end = 10.dp).wrapContentHeight(),
                                checked = isChecked,
                                enabled = isEnabled and isClickable,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Black,
                                    uncheckedColor = Color.Gray
                                ),
                                onCheckedChange = { check ->
                                    checkBoxs[bxIndex] = checkBoxs[bxIndex].copy(third = check)
                                    onCheckboxSelected(checkBoxs.filter { it.third==true }.map { it.second })
                                })
                        }
                            Text(
                                text = bxTitle?:"",
                                maxLines = if (isSingleLine) 1 else 100,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,

                                    ),
                                modifier = Modifier.fillMaxWidth()
                            )
                    }
                    if (bxIndex!=checkBoxs.size-1){
                        DottedLine(step = 10.dp,
                            padding = PaddingValues(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
        }
    }

}