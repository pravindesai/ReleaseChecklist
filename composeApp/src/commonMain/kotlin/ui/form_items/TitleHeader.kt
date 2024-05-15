package ui.form_items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleHeader(
    modifier: Modifier = Modifier,
    title: String,
    isRequired: Boolean = false,
    isEnabled: Boolean = true,
    isClickable: Boolean = true,
    toolTipText: String = "",
    isCaseComment: Boolean = false,
    isCaseCommentSwitchVisible: Boolean = false,
    isCaseCommentChecked: Boolean = false,
    onToolTipClicked: () -> Unit = {},
    onCaseCommentCheckClicked: (isChecked: Boolean) -> Unit = {}
) {
    val annotatedTitle = buildAnnotatedString {
        append(title)
        withStyle(style = SpanStyle(Color.Red)) {
            append(" *".takeIf { isRequired } ?: "")
        }
    }

    var caseCommentCheckState by remember { mutableStateOf(isCaseCommentChecked) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = annotatedTitle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,

                ),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 8.dp)
        )
    }

}

