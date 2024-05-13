package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.MAT_DARK
import colors.asColor
import repository.models.data.ObjDocument
import repository.models.data.ObjUser

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserView(
    objUser: ObjUser, onUserSelected: (ObjUser) -> Unit = {}, onUserDelete: (ObjUser) -> Unit = {}
) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = 2.dp,
        onClick = {
            onUserSelected(objUser)
        }) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(
                start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp
            )
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = (objUser.fields?.userid?.stringValue)
                    ?: (objUser.fields?.userId?.stringValue) ?: "",
                color = MAT_DARK.asColor(),
                style = TextStyle(
                    fontSize = 18.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Medium
                )
            )

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                tint = Color.LightGray,
                modifier = Modifier.height(24.dp).width(24.dp).clickable {
                    onUserDelete(objUser)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DocumentView(
    objDocument: ObjDocument,
    onDocumentSelected: (ObjDocument) -> Unit = {},
    onDocumentDelete: (ObjDocument) -> Unit = {}
) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = 2.dp,
        onClick = {
            onDocumentSelected(objDocument)
        }) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(
                start = 15.dp, end = 15.dp, top = 15.dp, bottom = 15.dp
            )
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = (objDocument.fields?.projectName?.stringValue)
                    ?: (objDocument.fields?.projectId?.stringValue) ?: "",
                color = MAT_DARK.asColor(),
                style = TextStyle(
                    fontSize = 18.sp, fontStyle = FontStyle.Normal, fontWeight = FontWeight.Medium
                )
            )

            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "",
                tint = Color.LightGray,
                modifier = Modifier.height(24.dp).width(24.dp).clickable {
                    onDocumentDelete(objDocument)
                }
            )
        }
    }
}