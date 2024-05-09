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
fun UsersListForAdmin(modifier: Modifier = Modifier, usersList: List<ObjUser>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        state = rememberLazyListState()
    ) {
        items(usersList) {
            Column {
                UserView(it, onUserSelected = { user: ObjUser ->

                })
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}

@Composable
fun ProjectListForAdmin(modifier: Modifier = Modifier, usersList: List<ObjDocument>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(5.dp),
        state = rememberLazyListState()
    ) {
        items(usersList) {
            Column {
                DocumentView(it, onUserSelected = { document: ObjDocument ->

                })
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}