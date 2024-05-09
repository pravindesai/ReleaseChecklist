package ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import colors.MAT_DARK
import colors.PRIMARY_ORANGE
import colors.asColor

@Composable
fun AddProjectDialog(
    onDoneClick: (projectName: String) -> Unit = { _ -> },
    onDismissClick: () -> Unit = {}
) {
    var projectName by remember { mutableStateOf("") }
    var showErrorForProjectName by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            onDismissClick()
        },
        content = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.animateContentSize()) {
                Card(
                    modifier = Modifier.padding(10.dp).wrapContentSize(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(15.dp)) {
                        OutlinedTextField(
                            value = projectName?:"",
                            isError = showErrorForProjectName,
                            onValueChange = {
                                showErrorForProjectName = false
                                projectName = it
                            },
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            singleLine = true,
                            label = {
                                androidx.compose.material3.Text("Enter organization")
                            },
                            textStyle = TextStyle(
                                color = colors.MAT_DARK.asColor(),
                                fontSize = 16.sp
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                if (projectName.isBlank()) {
                                    showErrorForProjectName = true
                                    return@KeyboardActions
                                }

                                onDoneClick(projectName)

                            }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.PRIMARY_ORANGE.asColor(),
                                unfocusedBorderColor = colors.MAT_DARK.asColor(),
                                focusedLabelColor = colors.PRIMARY_ORANGE.asColor(),
                                unfocusedLabelColor = colors.MAT_DARK.asColor()
                            ),
                            supportingText = {
                                if (showErrorForProjectName) {
                                    androidx.compose.material3.Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Please enter project name",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Add",
                            color = PRIMARY_ORANGE.asColor(),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.wrapContentWidth().clickable {
                                if (projectName.isBlank()) {
                                    showErrorForProjectName = true
                                    return@clickable
                                }

                                onDoneClick(projectName)
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Cancel",
                            color = PRIMARY_ORANGE.asColor(),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.wrapContentWidth().clickable {
                                onDismissClick()
                            }
                        )
                    }

                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )

}