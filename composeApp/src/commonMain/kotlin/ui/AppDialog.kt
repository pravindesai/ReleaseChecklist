package ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    showNegativeButton:Boolean = true,
    showPositiveButton:Boolean = true,
    negativeButtonText:String = "Dismiss",
    positiveButtonText:String = "Confirm",
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector? = null,
    ) {
    AlertDialog(
        modifier = modifier,
        icon = {
            icon?.let {
                Icon(icon, contentDescription = "Example Icon")
            }
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            if (showPositiveButton){
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text(positiveButtonText)
                }
            }
        },
        dismissButton = {
            if (showNegativeButton){
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(negativeButtonText)
                }
            }
        }
    )
}