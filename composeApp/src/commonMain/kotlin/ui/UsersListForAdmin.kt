package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import repository.models.data.ObjDocument
import repository.models.data.ObjUser

@Composable
fun UsersListForAdmin(
    modifier: Modifier = Modifier,
    usersList: List<ObjUser>,
    onUserSelected: (ObjUser) -> Unit = {},
    onUserDelete: (ObjUser) -> Unit = {}
) {
    val layzListState = rememberLazyListState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        state = layzListState
    ) {
        items(usersList) {
            Column {
                UserView(objUser = it,
                    delay = (0..10).random().toLong(),
                    onUserSelected = { user: ObjUser ->
                    onUserSelected(user)
                }, onUserDelete = { user: ObjUser ->
                    onUserDelete(user)
                })
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

@Composable
fun ProjectListForAdmin(
    modifier: Modifier = Modifier,
    usersList: List<ObjDocument>,
    onDocumentSelected: (ObjDocument) -> Unit = {},
    onDocumentDelete: (ObjDocument) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        state = rememberLazyListState()
    ) {
        items(usersList) {
            Column {
                DocumentView(objDocument = it,
                    delay = (0..10).random().toLong(),
                    onDocumentSelected = { document: ObjDocument ->
                    onDocumentSelected(document)
                }, onDocumentDelete = { document: ObjDocument ->
                    onDocumentDelete(document)
                })
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}