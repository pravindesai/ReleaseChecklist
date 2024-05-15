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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    title: String? = "",
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isClickable: Boolean = true,
    helpText: String = "",
    dropdownItems: List<String> = emptyList(),
    selectedIndex: Int? = null,
    onItemsSelected: (index: Int?) -> Unit = { _ -> },
    onToolTipClicked: () -> Unit = {},
    showDialog: (dialog: @Composable() () -> Unit) -> Unit = {},
    onDoneClick: (index: Int?) -> Unit = {},
    onBackClick: () -> Unit = { },

    ) {

    var selectedIndexState by remember { mutableStateOf(selectedIndex) }
    LaunchedEffect(key1 = selectedIndex, block ={
        selectedIndexState = selectedIndex
    } )


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
            title = title ?: "",
            isRequired = isRequired,
            toolTipText = helpText ?: "",
            isEnabled = true,
            isClickable = true,
            onToolTipClicked = { onToolTipClicked() }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
        )
        DropdownSpinnerView(
            isEnabled = isEnabled,
            isClickable = isClickable,
            dropdownItems = dropdownItems,
            selectedIndex = selectedIndexState,
            onItemsSelected = onItemsSelected,
            showDialog = showDialog,
            onBackClick = onBackClick,
            onDoneClick = onDoneClick,
            title = title ?: ""
        )

    }
}


@Composable
fun DropdownSpinnerView(
    isEnabled: Boolean = true,
    isClickable: Boolean = true,
    title: String = "",
    dropdownItems: List<String> = emptyList(),
    selectedIndex: Int? = null,
    onItemsSelected: (index: Int?) -> Unit = { _ -> },
    showDialog: (dialog: @Composable() () -> Unit) -> Unit = {},
    onDoneClick: (index: Int?) -> Unit = {},
    onBackClick: () -> Unit = { },
) {
    var selectedIndexState by remember { mutableStateOf(selectedIndex) }
    LaunchedEffect(key1 = selectedIndex, block ={
        selectedIndexState = selectedIndex
    } )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(enabled = isEnabled && isClickable) {
                showDialog {
                    DropdownSingleSelectorView(
                        dropdownItems = dropdownItems,
                        selectedIndex = selectedIndexState,
                        title = title,
                        onDoneClick = { selectedIn ->
                            selectedIndexState = selectedIn
                            onDoneClick(selectedIndexState)
                        },
                        onBackClick = onBackClick,
                        onItemsSelected = {
                            selectedIndexState = it
                            onItemsSelected(it)
                        }
                    )
                }
            },
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        border = BorderStroke(1.dp, color = colors.MAT_DARK.asColor()),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 13.dp, bottom = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedIndexState == null) "Select Items" else dropdownItems.get(
                    selectedIndexState ?: 0
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,

                    ),
                modifier = Modifier.wrapContentWidth()
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                modifier = Modifier
                    .height(14.dp)
                    .width(14.dp)
                    .rotate(90f),
                contentDescription = ""
            )

        }
    }
}

@Composable
fun DropdownSingleSelectorView(
    title: String = "",
    dropdownItems: List<String> = emptyList(),
    selectedIndex: Int? = null,
    onItemsSelected: (index: Int?) -> Unit = { _ -> },
    onDoneClick: (index: Int?) -> Unit = {},
    onBackClick: () -> Unit = {},
    onSearch: (text: String) -> Unit = {}
) {

    var selectedIndexState by remember { mutableStateOf(selectedIndex) }
    var dropdownItemsState by remember { mutableStateOf(dropdownItems) }
    var allDropDownItems by remember { mutableStateOf(dropdownItems) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(key1 = searchText, block = {
        dropdownItemsState = if (searchText.isBlank()) {
            allDropDownItems.toList()
        } else {
            allDropDownItems.filter { it.contains(searchText, ignoreCase = true) }.toList()
        }
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = colors.FLOATING_BTN_COLOR.asColor())
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 20.dp,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    modifier = Modifier
                        .height(18.dp)
                        .width(19.dp)
                        .rotate(180f)
                        .clickable {
                            searchText = ""
                            onBackClick()
                        },
                    tint = colors.MAT_DARK.asColor(),
                    contentDescription = ""
                )

                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,

                        ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Done",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.MAT_DARK.asColor()
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            searchText = ""
                            onDoneClick(selectedIndexState)
                        }
                )

            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = colors.EXTRA_LIGHT_BG.asColor())
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp,
                        bottom = 20.dp,
                    )
            ) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                    content = {
                        Row {
                            OutlinedTextField(
                                value = searchText,
                                singleLine = true,
                                maxLines = 1,
                                onValueChange = {
                                    searchText = it
                                    onSearch(searchText)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 0.dp,
                                        end = 0.dp,
                                    ),
                                shape = RoundedCornerShape(8.dp),
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "",
                                        tint = Color.LightGray,
                                        modifier = Modifier
                                            .height(24.dp)
                                            .width(24.dp)
                                    )
                                },
                                textStyle = TextStyle.Default.copy(fontSize = 14.sp),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                )
                            )
                        }
                    }
                )
            }

        }
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),

                state = rememberLazyListState(),
                content = {
                    items(
                        items = dropdownItemsState,
                        itemContent = {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(
                                        start = 10.dp,
                                        end = 10.dp,
                                        top = 7.dp,
                                        bottom = 7.dp
                                    )
                                    .clickable {
                                        selectedIndexState = allDropDownItems.indexOf(it)
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "",
                                    tint = if (selectedIndexState == allDropDownItems.indexOf(it)) colors.MAT_DARK.asColor() else Color.Transparent,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .height(24.dp)
                                        .width(24.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = it,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ), modifier = Modifier
                                        .wrapContentWidth()
                                        .padding(3.dp)
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .height(1.dp)
                                    .fillMaxWidth(),
                                color = Color.LightGray
                            )

                        })
                }
            )
        }
    }

}