package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import repository.models.data.ObjDocument

@Composable
fun ReleasesTabContainer(modifier: Modifier = Modifier,
                         releasesList:List<ObjDocument>) {
    var releasesListState by remember { mutableStateOf(releasesList) }

    LaunchedEffect(releasesList){
        releasesListState = releasesList
    }

    Column(modifier = modifier.fillMaxWidth()) {
        ReleaseItemsList(
            listOfReleases = releasesListState,
            onCardClick = { doc ->

            },
            onDeleteClick = { doc ->

            }
        )
    }

}