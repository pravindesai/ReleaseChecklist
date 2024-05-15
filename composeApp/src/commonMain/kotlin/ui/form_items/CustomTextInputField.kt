package ui.form_items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import colors.asColor
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextInputField(
    modifier: Modifier = Modifier,
    text: String = "",
    focusManager: FocusManager = LocalFocusManager.current,
    focusRequester:FocusRequester = FocusRequester(),
    maxCharCount: Int = 100,
    viewType: String? = null,
    patternType: String? = "Text",
    isEnabled: Boolean = true,
    isClickable: Boolean = true,
    placeholder: String = "",
    minLines: Int = 1,
    allowAutoComplete: Boolean = false,
    autoCompleteList: List<String> = emptyList<String>(),
    singleLine: Boolean = true,
    onTextChanged: (updatedText: String, isTextSelectedFromAutoSearch: Boolean) -> Unit = { _, _ -> },
    onEdtClicked: () -> Unit = {},
    onFocusChangedListener: (isInFocused: Boolean, isValueChanged: Boolean) -> Unit = { _, _ -> },
    onDoneClicked: () -> Unit = {}
) {
    val autoSearchThreshold = 2
    var previousText by remember { mutableStateOf(text) }
    var isAutoSearchTriggered by remember { mutableStateOf(false) }
    val searchResults by remember { mutableStateOf(mutableListOf<String>()) }
    var isInFocus by remember { mutableStateOf(false) }
    val isPhoneNumber by remember { mutableStateOf(false) }
    val isCurrency by remember { mutableStateOf(patternType=="Currency") }
    var isValueChanged by remember { mutableStateOf(false) }
    val keyboardOptions by remember {
        mutableStateOf(
            getKeyboardOption(
                fieldType = viewType,
                patternType = patternType
            )
        )
    }
    var textFieldValue by remember { mutableStateOf(text) }


    val interactionSource = remember {
        object : MutableInteractionSource {
            override val interactions = MutableSharedFlow<Interaction>(
                extraBufferCapacity = 16,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

            override suspend fun emit(interaction: Interaction) {
                when (interaction) {
                    is PressInteraction.Press -> {
                        onEdtClicked()
                    }
                }

                interactions.emit(interaction)
            }

            override fun tryEmit(interaction: Interaction): Boolean {
                return interactions.tryEmit(interaction)
            }
        }
    }



    OutlinedTextField(
        value = textFieldValue,
        singleLine = singleLine,
        enabled = isEnabled || isClickable,
        maxLines = 4,
        interactionSource = interactionSource,
        visualTransformation = VisualTransformation.None,
        label = {},
        placeholder = {
            Text(text = placeholder)
        },
        onValueChange = { value ->
            if (textFieldValue != value) {
                value.let { changedText ->
                    if ((isPhoneNumber && changedText.filter { it.isDigit() }.length > 10) ||
                        isCurrency && changedText.length>9) {
                        return@let
                    }
                    try {
                        if ((changedText.length <= maxCharCount) ||
                            (changedText.last() != '\n' && changedText.length == 1)
                        ) {
                            previousText = textFieldValue
                            val updatedText = changedText
                            isValueChanged = true
                            textFieldValue = updatedText
                            onTextChanged(
                                textFieldValue, false
                            )

                            //search list
                            searchResults.clear()
                            searchResults.addAll(autoCompleteList.filter { filtertext ->
                                filtertext.contains(
                                    changedText, ignoreCase = true
                                )
                            })
                            if (changedText.length > 2 && allowAutoComplete) {
                                isAutoSearchTriggered = true
                            }
                        }
                    } catch (e: Exception) {
                        println("--Exception "+ e.message.toString())
                    }
                }
            }
        },
        modifier = modifier
            .clipToBounds()
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable(enabled = true, interactionSource = interactionSource)
            .clickable(enabled = true, onClick = {
                onEdtClicked()
            })
            .border(
                border = BorderStroke(
                    width = 1.dp, color = Color.Transparent
                ), shape = RoundedCornerShape(size = 12.dp)
            )
            .onFocusChanged {
                isInFocus = it.hasFocus
//                val isValueChanged = textFieldValue != previousText

/*                if (isValueChanged and isInFocus.not()) {
                    focusManager.clearFocus()
                }
                onFocusChangedListener(isInFocus, isValueChanged)
                isValueChanged = false*/
            }
            .defaultMinSize(
                minHeight = if (minLines > 1) 120.dp else 50.dp
            )
            .padding(
                start = 0.dp,
                end = 0.dp,
            )
            .focusable(true),
        textStyle = TextStyle(
            fontSize = 16.sp
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onDoneClicked()
        }),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colors.MAT_DARK.asColor(),
            unfocusedBorderColor = colors.MAT_DARK.asColor()
        )
    )
}



fun getKeyboardOption(fieldType:String?=null, patternType: String?=null): KeyboardOptions {
    return when (fieldType) {
        CustomViewUtils.FIELD_TYPE_TEXTAREA -> {
            KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = getKeyboardTypeByPattern(patternType),
                imeAction = ImeAction.Done
            )
        }
        CustomViewUtils.FIELD_TYPE_EMAIL -> { KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ) }
//            set Phone Number
        CustomViewUtils.FIELD_TYPE_PHONENUMBER -> { KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
        } else -> {
            KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = getKeyboardTypeByPattern(patternType),
                imeAction = ImeAction.Next
            )
        }
    }
}

fun getKeyboardTypeByPattern(patternType:String?): KeyboardType {
    return when(patternType){
        "Text" -> KeyboardType.Text
        "Currency","Number","Percentage" -> KeyboardType.Number
        else -> KeyboardType.Text
    }
}
