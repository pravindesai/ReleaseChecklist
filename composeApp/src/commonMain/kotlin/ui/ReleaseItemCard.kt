package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.EXTRA_LIGHT_GRAY
import colors.GREEN
import colors.LIGHT_GRAY
import colors.MAT_DARK
import colors.MAT_DARK_LIGHT
import colors.RED
import colors.asColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import repository.models.data.ObjDocument

@Composable
fun ReleaseItemsList(
    modifier: Modifier = Modifier,
    listOfReleases: List<ObjDocument>,
    isDeleteAllowed:Boolean = false,
    defaultClosed:Boolean = false,
    onCardClick: (objDoc: ObjDocument) -> Unit = {},
    onDeleteClick: (objDoc: ObjDocument) -> Unit = {}
) {
    var listOfReleasesState by remember { mutableStateOf(listOfReleases) }
    LaunchedEffect(listOfReleases) {
        listOfReleasesState = listOfReleases.map {
            if (defaultClosed){
                it.isExpanded = false
            }
            it
        }
    }
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier.padding(start = 5.dp, end = 5.dp)
    ) {
        items(listOfReleasesState) {
            ReleaseItem(
                release = it,
                isDeleteAllowed = isDeleteAllowed,
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
            if (listOfReleasesState.lastOrNull() == it) {
                Spacer(modifier = Modifier.height(80.dp))
            } else {
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

@Composable
fun ReleaseItem(
    modifier: Modifier = Modifier,
    release: ObjDocument,
    isDeleteAllowed:Boolean = false,
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
                objDoc = releaseState,
                isDeleteAllowed = isDeleteAllowed,
                onDeleteClick = {
                    onDeleteClick(it)
                },
                onHeaderClicked = {
                    releaseState = releaseState.copy(isExpanded = releaseState.isExpanded.not())
                    onExpandClick(releaseState)
                },
                onExpandClick = {
                    releaseState = it
                    onExpandClick(it)
                })

            if(releaseState.isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    RowOfTable(
                        position = 1,
                        key = "Project",
                        value = releaseState.fields?.projectName?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 2,
                        key = "Branch",
                        value = releaseState.fields?.branchName?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 3,
                        key = "Version",
                        value = releaseState.fields?.releaseId?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 4,
                        key = "Released On",
                        value = releaseState.fields?.releaseDateTimeEpoch?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 5,
                        key = "Tag",
                        value = releaseState.fields?.tag?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 6,
                        key = "Released By",
                        value = releaseState.fields?.userId?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 7,
                        key = "Admin Id",
                        value = releaseState.fields?.adminId?.stringValue ?: "N/A"
                    )
                    RowOfTable(
                        position = 8,
                        key = "ControlCenter ID",
                        value = if (releaseState.fields?.baseCampIDChecked?.booleanValue == true) "Checked" else "Not Checked",
                        isWarning = (releaseState.fields?.baseCampIDChecked?.booleanValue == true)
                    )
                    RowOfTable(
                        position = 9,
                        key = "ControlCenter Log",
                        value = if (releaseState.fields?.baseCampLogCheked?.booleanValue == true) "Checked" else "Not Checked",
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
    isDeleteAllowed:Boolean = false,
    onDeleteClick: (objDoc: ObjDocument) -> Unit,
    onHeaderClicked: (objDoc: ObjDocument) -> Unit,
    onExpandClick: (objDoc: ObjDocument) -> Unit
) {
    var objDocState by remember { mutableStateOf(objDoc) }
    LaunchedEffect(objDoc) {
        objDocState = objDoc
    }
    val coroutineScope = rememberCoroutineScope()
    var visibilityState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        coroutineScope.launch {
            delay(15)
            visibilityState = true
        }
    }

    AnimatedContent(
        targetState = visibilityState,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                ),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(200),
                        targetOffsetX = { fullWidth -> fullWidth }
                    )
        }
    ) { targetState ->
        Row(
            modifier = Modifier
                .background(if (targetState) MAT_DARK.asColor() else Color.Transparent)
                .padding(1.dp)
                .background(if (targetState)  Color.White else Color.Transparent)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp).clickable {
                    onHeaderClicked(objDocState)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (targetState) title else "",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .padding(2.dp),
                fontWeight = FontWeight.Bold,
                color = MAT_DARK.asColor(),
                textAlign = TextAlign.Start,
            )


            Row {
                if (isDeleteAllowed){
                    Icon(
                        modifier = Modifier.size(24.dp)
                            .clickable {
                                onDeleteClick(objDocState)
                            },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = if (targetState) MAT_DARK.asColor() else Color.Transparent
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier.size(24.dp).rotate(if (objDocState.isExpanded) 0f else 180f)
                        .clickable {
                            objDocState = objDocState.copy(isExpanded = objDocState.isExpanded.not())
                            onExpandClick(objDocState)
                        },
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "",
                    tint = if (targetState) MAT_DARK.asColor() else Color.Transparent
                )
            }
        }
    }


}

@Composable
fun RowOfTable(position:Int = 0, key: String, value: String, isWarning: Boolean? = null) {
    val coroutineScope = rememberCoroutineScope()
    var visibilityState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        coroutineScope.launch {
            delay(position.times(15).toLong())
            visibilityState = true
        }
    }
    AnimatedContent(
        targetState = visibilityState,
        transitionSpec = {
            slideInHorizontally(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow,
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    visibilityThreshold = IntOffset.VisibilityThreshold
                ),
                initialOffsetX = { fullWidth -> fullWidth }
            ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(150),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
        }
    ) { targetState ->
        Row(
            modifier = Modifier
                .background(color = if (targetState) colors.MAT_DARK_LIGHT.asColor() else Color.Transparent)
                .padding(start = (0.5).dp, end = (0.5).dp, bottom = (0.5).dp)
                .background(if (targetState)  Color.White else Color.Transparent)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp)
        ) {
            Text(
                text = if (targetState) key else "",
                fontSize = 16.sp,
                modifier = Modifier.weight(1f).padding(2.dp),
                fontWeight = FontWeight.Medium,
                color = MAT_DARK.asColor(),
                textAlign = TextAlign.Start,
            )

            Text(
                text = if (targetState) value else "",
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

}
