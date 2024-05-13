package ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.GREEN
import colors.MAT_DARK
import colors.MAT_DARK_LIGHT
import colors.RED
import colors.asColor
import repository.models.data.ObjDocument

@Composable
fun ReleaseItemsList(
    modifier: Modifier = Modifier,
    listOfReleases: List<ObjDocument>,
    onCardClick: (objDoc: ObjDocument) -> Unit = {},
    onDeleteClick: (objDoc: ObjDocument) -> Unit = {}
) {
    var listOfReleasesState by remember { mutableStateOf(listOfReleases) }
    LaunchedEffect(listOfReleases) {
        listOfReleasesState = listOfReleases
    }
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        items(listOfReleasesState) {
            ReleaseItem(
                release = it,
                onCardClick = { doc ->
                    onCardClick(doc)
                },
                onDeleteClick = { doc ->
                    onDeleteClick(doc)
                },
                onExpandClick = { doc ->
                    val newList = mutableListOf<ObjDocument>()
                    newList.addAll(listOfReleasesState)
                    listOfReleasesState = newList
                }
            )
            if (listOfReleasesState.lastOrNull() == it){
                Spacer(modifier = Modifier.height(80.dp))
            }else{
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun ReleaseItem(
    modifier: Modifier = Modifier,
    release: ObjDocument,
    onCardClick: (objDoc: ObjDocument) -> Unit = {},
    onDeleteClick: (objDoc: ObjDocument) -> Unit = {},
    onExpandClick: (objDoc: ObjDocument) -> Unit = {}
) {
    var releaseState by remember { mutableStateOf(release) }
    LaunchedEffect(release) {
        releaseState = release
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                           onCardClick(releaseState)
                },
        ) {

            RowTableTitleHeader(
                title = releaseState.fields?.releaseId?.stringValue ?: "",
                releaseState,
                onHeaderClicked = {
                    releaseState = releaseState.copy(isExpanded = releaseState.isExpanded.not())
                    onExpandClick(releaseState)
                },
                onExpandClick = {
                    releaseState = it
                    onExpandClick(it)
                })

            AnimatedVisibility(releaseState.isExpanded) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    RowOfTable("Project", releaseState.fields?.projectName?.stringValue ?: "N/A")
                    RowOfTable("Branch", releaseState.fields?.branchName?.stringValue ?: "N/A")
                    RowOfTable("Version", releaseState.fields?.releaseId?.stringValue ?: "N/A")
                    RowOfTable(
                        "Released On",
                        releaseState.fields?.releaseDateTimeEpoch?.stringValue ?: "N/A"
                    )
                    RowOfTable("Tag", releaseState.fields?.tag?.stringValue ?: "N/A")
                    RowOfTable("Released By", releaseState.fields?.userId?.stringValue ?: "N/A")
                    RowOfTable("Admin Id", releaseState.fields?.adminId?.stringValue ?: "N/A")
                    RowOfTable(
                        "ControlCenter ID",
                        if (releaseState.fields?.baseCampIDChecked?.booleanValue == true) "Checked" else "Not Checked",
                        isWarning = (releaseState.fields?.baseCampIDChecked?.booleanValue == true)
                    )
                    RowOfTable(
                        "ControlCenter Log",
                        if (releaseState.fields?.baseCampLogCheked?.booleanValue == true) "Checked" else "Not Checked",
                        isWarning = (releaseState.fields?.baseCampLogCheked?.booleanValue == true)
                    )
                }
            }
        }
    }
}

@Composable
fun RowTableTitleHeader(
    title: String,
    objDoc: ObjDocument,
    onHeaderClicked: (objDoc: ObjDocument) -> Unit,
    onExpandClick: (objDoc: ObjDocument) -> Unit
) {
    var objDocState by remember { mutableStateOf(objDoc) }
    LaunchedEffect(objDoc) {
        objDocState = objDoc
    }
    Row(
        modifier = Modifier
            .background(MAT_DARK.asColor())
            .padding(1.dp)
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(6.dp).clickable {
                                     onHeaderClicked(objDocState)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(2.dp),
            fontWeight = FontWeight.Bold,
            color = MAT_DARK.asColor(),
            textAlign = TextAlign.Start,
        )


        Icon(
            modifier = Modifier.size(24.dp).rotate(if (objDocState.isExpanded) 0f else 180f)
                .clickable {
                    objDocState = objDocState.copy(isExpanded = objDocState.isExpanded.not())
                    onExpandClick(objDocState)
                },
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "",
            tint = MAT_DARK.asColor()
        )
    }
}

@Composable
fun RowOfTable(key: String, value: String, isWarning: Boolean? = null) {
    Row(
        modifier = Modifier
            .background(MAT_DARK_LIGHT.asColor())
            .padding(start = (0.5).dp, end = (0.5).dp, bottom = (0.5).dp)
            .background(Color.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(6.dp)
    ) {
        Text(
            text = key,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f).padding(2.dp),
            fontWeight = FontWeight.Medium,
            color = MAT_DARK.asColor(),
            textAlign = TextAlign.Start,
        )

        Text(
            text = value,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f).padding(2.dp),
            fontWeight = FontWeight.Normal,
            color = when (isWarning?.not()) {
                true -> RED.asColor()
                false -> GREEN.asColor()
                else -> MAT_DARK.asColor()
            },
            textAlign = TextAlign.Start,
        )
    }
}
