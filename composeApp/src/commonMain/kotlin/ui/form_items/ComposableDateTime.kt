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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.TimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeComposable(
    modifier: Modifier = Modifier,
    title: String = "",
    dateTimeString: String,
    isClickable: Boolean = true,
    isEnabled: Boolean = true,
    isRequired: Boolean = false,
    helpText: String = "",
    onDateTimeSelected: (dateTime: String) -> Unit = {},
    onToolTipClicked: () -> Unit = {}
) {


    var datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    var timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

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
                    showDatePicker = true
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
                            start = 5.dp,
                            end = 10.dp,
                            top = 12.dp,
                            bottom = 12.dp
                        )
                        .background(color = Color.Transparent),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = dateTimeString,
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

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        modifier = Modifier.size(24.dp),
                        contentDescription = "",
                        tint = colors.MAT_DARK.asColor()
                    )
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                confirmButton = {
                    Text(text = "Confirm", modifier = Modifier.clickable {
                        showTimePicker = true
                        showDatePicker = false
                    }.padding(5.dp), fontWeight = FontWeight.Bold)
                },
                dismissButton = {
                    Text(text = "Cancel", modifier = Modifier.clickable {
                        showDatePicker = false
                    }.padding(5.dp), fontWeight = FontWeight.Bold)
                },
                onDismissRequest = {
                    showDatePicker = false
                },

                )
            {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            TimePickerDialog(
                onConfirm = {
                    showTimePicker = false
                    val date = Instant.fromEpochMilliseconds(
                        datePickerState.selectedDateMillis ?: Clock.System.now()
                            .toEpochMilliseconds()
                    ).toLocalDateTime(
                        TimeZone.UTC
                    ).date.run {
                        "${this.dayOfMonth}/${this.month}/${this.year}"
                    }
                    val time = "${timePickerState.hour}:${timePickerState.minute}"
                    onDateTimeSelected("$date - $time")
                },
                onCancel = {
                    showTimePicker = false
                },
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}