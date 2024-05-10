package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.MAT_DARK
import colors.PRIMARY_ORANGE
import colors.asColor

@Composable
fun CustomViewTypeSelectionTab(
    modifier: Modifier = Modifier,
    selectedType:AdminListViewType,
    onViewTypeChanged:(AdminListViewType) -> Unit = {}
){
    Column(modifier = modifier.wrapContentHeight().fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            elevation = 0.dp, shape = RoundedCornerShape(0),
            backgroundColor = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(0.5f).animateContentSize()) {
                    Text(
                        text = AdminListViewType.Users.toString(),
                        color = if (selectedType == AdminListViewType.Users) PRIMARY_ORANGE.asColor() else MAT_DARK.asColor(),
                        style = TextStyle(
                            fontSize = if (selectedType == AdminListViewType.Users) 20.sp else 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = if (selectedType == AdminListViewType.Users) FontWeight.Bold else FontWeight.Light
                        ),
                        modifier = Modifier.fillMaxWidth().clickable {
                            onViewTypeChanged(AdminListViewType.Users)
                        }
                    )

                    AnimatedVisibility(selectedType == AdminListViewType.Users){
                        Divider(modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp), color = PRIMARY_ORANGE.asColor(), thickness = 1.dp)
                    }
                }

                Column(modifier = Modifier.weight(0.5f).animateContentSize()) {
                    Text(
                        text = AdminListViewType.Projects.toString(),
                        color = if (selectedType == AdminListViewType.Projects) PRIMARY_ORANGE.asColor() else MAT_DARK.asColor(),
                        style = TextStyle(
                            fontSize = if (selectedType == AdminListViewType.Projects) 20.sp else 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = if (selectedType == AdminListViewType.Projects) FontWeight.Bold else FontWeight.Light
                        ),
                        modifier = Modifier.fillMaxWidth().clickable {
                            onViewTypeChanged(AdminListViewType.Projects)
                        }
                    )
                    AnimatedVisibility(selectedType == AdminListViewType.Projects){
                        Divider(modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp), color = PRIMARY_ORANGE.asColor(), thickness = 1.dp)
                    }

                }

                Column(modifier = Modifier.weight(0.5f).animateContentSize()) {
                    Text(
                        text = AdminListViewType.Releases.toString(),
                        color = if (selectedType == AdminListViewType.Releases) PRIMARY_ORANGE.asColor() else MAT_DARK.asColor(),
                        style = TextStyle(
                            fontSize = if (selectedType == AdminListViewType.Releases) 20.sp else 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = if (selectedType == AdminListViewType.Releases) FontWeight.Bold else FontWeight.Light
                        ),
                        modifier = Modifier.fillMaxWidth().clickable {
                            onViewTypeChanged(AdminListViewType.Releases)
                        }
                    )
                    AnimatedVisibility(selectedType == AdminListViewType.Releases){
                        Divider(modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp), color = PRIMARY_ORANGE.asColor(), thickness = 1.dp)
                    }
                }
            }
        }
    }
}