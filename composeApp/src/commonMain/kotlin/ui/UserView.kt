package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
fun UserView(objUser: ObjUser, onUserSelected: (ObjUser) -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = 2.dp,
        onClick = {
            onUserSelected(objUser)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            Text(
                text = (objUser.fields?.userid?.stringValue)
                    ?: (objUser.fields?.userId?.stringValue) ?: "",
                color = MAT_DARK.asColor(),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DocumentView(objDocument: ObjDocument, onUserSelected: (ObjDocument) -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        shape = RoundedCornerShape(5.dp),
        elevation = 2.dp,
        onClick = {
            onUserSelected(objDocument)
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(
                start = 10.dp,
                end = 10.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            Text(
                text = (objDocument.fields?.projectName?.stringValue)
                    ?: (objDocument.fields?.projectId?.stringValue) ?: "",
                color = MAT_DARK.asColor(),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}