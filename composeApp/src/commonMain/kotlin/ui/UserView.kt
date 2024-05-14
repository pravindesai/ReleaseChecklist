package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.LIGHT_GRAY
import colors.MAT_DARK
import colors.MAT_WHITE
import colors.asColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import screens.mainscreen.UserSelectionScreen

@Composable
fun UserView(
    objUser: ObjUser,
    delay: Long = 1,
    onUserSelected: (ObjUser) -> Unit = {},
    onUserDelete: (ObjUser) -> Unit = {}
) {

    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(10.times(delay))
            state = true
        }
    }

    AnimatedContent(
        targetState = state,
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
                        animationSpec = tween(200),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
        }
    ) { targetState ->

        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            PlainTextTile(
                modifier = Modifier.background(color = if (targetState) colors.LIGHT_GRAY.asColor() else Color.Transparent)
                    .padding(top = 20.dp, bottom = 20.dp),
                textColor = MAT_WHITE.asColor(),
                text = (objUser.fields?.userid?.stringValue)
                    ?: (objUser.fields?.userId?.stringValue) ?: "",
                onClick = {
                    onUserSelected(objUser)
                }
            )
        }


        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .padding(top = 20.dp, bottom = 20.dp, end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                tint = colors.MAT_WHITE.asColor(),
                modifier = Modifier.height(24.dp).width(24.dp).clickable {
                    onUserDelete(objUser)
                }
            )
        }


    }


}


@Composable
fun DocumentView(
    objDocument: ObjDocument,
    delay: Long = 1,
    onDocumentSelected: (ObjDocument) -> Unit = {},
    onDocumentDelete: (ObjDocument) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var state by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(10.times(delay))
            state = true
        }
    }

    AnimatedContent(
        targetState = state,
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
                        animationSpec = tween(200),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
        }
    ) { targetState ->

        Row(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            PlainTextTile(
                modifier = Modifier.background(color = if (targetState) colors.LIGHT_GRAY.asColor() else Color.Transparent)
                    .padding(top = 20.dp, bottom = 20.dp),
                textColor = MAT_WHITE.asColor(),
                text = (objDocument.fields?.projectName?.stringValue)
                    ?: (objDocument.fields?.projectId?.stringValue) ?: "",
                onClick = {
                    onDocumentSelected(objDocument)
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
                .padding(top = 20.dp, bottom = 20.dp, end = 10.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                tint = colors.MAT_WHITE.asColor(),
                modifier = Modifier.height(24.dp).width(24.dp).clickable {
                    onDocumentDelete(objDocument)
                }
            )
        }
    }

}