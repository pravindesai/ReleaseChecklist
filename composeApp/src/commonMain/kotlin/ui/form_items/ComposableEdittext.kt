package ui.form_items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun ComposableEdittext(
    modifier: Modifier = Modifier,
    title: String = "email",
    isRequired: Boolean = true,
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester:FocusRequester = FocusRequester(),
    isEnabled: Boolean = true,
    isClickable: Boolean = true,
    text: String = "",
    placeholder: String = "",
    toolTipText: String = "tooltip text",
    maxCharCount: Int = 100,
    minLines:Int = 1,
    singleLine: Boolean = true,
    allowAutoComplete: Boolean = false,
    isInFocus: Boolean = false,
    autoCompleteList:List<String> = mutableListOf<String>(),
    keyboardOptions: KeyboardOptions? = null,
    viewType: String? = null,
    patternType: String? = "Text",
    isCaseComment:Boolean=false,
    isCaseCommentSwitchVisible:Boolean = false,
    isCaseCommentChecked:Boolean=false,
    onTextChanged: (updatedText: String, isTextSelectedFromAutoSearch:Boolean) -> Unit = {_,_->},
    onEdtClicked: () -> Unit = {},
    onFocusChangedListener: (isInFocused:Boolean, isValueChanged:Boolean) -> Unit = {_,_->},
    onToolTipClicked: (xOffset:Int, yOffset:Int) -> Unit = {_,_->},
    onDoneClicked: () -> Unit = {},
    onCaseCommentCheckClicked:(isChecked:Boolean)->Unit = {}
) {
    var charCount by remember{ mutableStateOf(text.length) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 8.dp, end = 8.dp, bottom = 18.dp
            )
            .background(color = Color.White)
    ) {
        TitleHeader(
            title = title,
            isRequired = isRequired,
            toolTipText = toolTipText,
            isCaseComment   = isCaseComment,
            isEnabled = isEnabled,
            isClickable = isClickable,
            isCaseCommentSwitchVisible    = isCaseCommentSwitchVisible,
            isCaseCommentChecked    = isCaseCommentChecked,
            onCaseCommentCheckClicked   = onCaseCommentCheckClicked,
            onToolTipClicked = {
//                onToolTipClicked(930,-270)
            }
        )

        CustomTextInputField(
            text = text,
            placeholder = placeholder,
            maxCharCount = maxCharCount,
            focusRequester = focusRequester,
            focusManager = focusManager,
            keyboardOptions = keyboardOptions,
            viewType = viewType,
            patternType = patternType,
            minLines = minLines,
            isEnabled = isEnabled,
            isClickable = isClickable,
            singleLine = singleLine,
            allowAutoComplete = allowAutoComplete,
            autoCompleteList = autoCompleteList,
            onTextChanged = {text, isFromAutoSearch->
                charCount = text.length
                onTextChanged(text, isFromAutoSearch)
            },
            onFocusChangedListener = onFocusChangedListener,
            onEdtClicked = onEdtClicked,
            onDoneClicked = onDoneClicked
        )
    }
}

