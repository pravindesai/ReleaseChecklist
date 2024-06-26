package screens.addReleaseScreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.asColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.BooleanValue
import repository.models.Fields
import repository.models.StringValue
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import ui.AppAlertDialog
import ui.AppProgressBar
import ui.ScreenHeaderWithTitle
import ui.form_items.ComposableEdittext
import ui.form_items.ComposableRadioGroup
import ui.form_items.DateTimeComposable
import ui.form_items.Dropdown
import ui.form_items.getKeyboardOption

class AddReleaseScreen : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        var isLoading by remember { mutableStateOf(false) }

        var dialogShowMsgNavigateback by remember { mutableStateOf(Triple(false, "", false)) }

        var dialogLayout by remember {
            mutableStateOf<((@Composable () -> Unit))>({
                Box(
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )
            })
        }

        val currentUser by remember {
            mutableStateOf(
                (CommonRepository.getLoggedInUser() as? ObjUser) ?: ObjUser()
            )
        }

        var listOfProjects by remember { mutableStateOf(emptyList<ObjDocument>()) }

        var projectName by remember { mutableStateOf("") }
        var branchName by remember { mutableStateOf("") }
        var version by remember { mutableStateOf("") }
        var releaseDateTime by remember { mutableStateOf("") }
        var tag by remember { mutableStateOf("") }
        val userId by remember {
            derivedStateOf {
                currentUser.fields?.userId?.stringValue ?: currentUser.fields?.userid?.stringValue
            }
        }
        val adminId by remember {
            derivedStateOf {
                currentUser.fields?.adminId?.stringValue ?: currentUser.fields?.adminid?.stringValue
            }
        }
        var controlCenterIDChecked by remember { mutableStateOf(false) }
        var controlCenterLogChecked by remember { mutableStateOf(false) }


        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false },
            skipHalfExpanded = false,
        )

        LaunchedEffect(listOfProjects) {
            coroutineScope.launch {
                isLoading = true
                val projectListResult = CommonRepository.getAllProjectForAdmin(
                    CommonRepository.getCurrentAdminId() ?: ""
                )
                if ((projectListResult?.success == true).not()) {
                    isLoading = false
                    val erroMsg = projectListResult?.message ?: "FAILED"
                    dialogShowMsgNavigateback = Triple(true, erroMsg, true)
                } else {
                    isLoading = false
                    listOfProjects = projectListResult?.data ?: emptyList()
                }
            }
        }

        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxSize(),
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(15.dp),
            sheetContent = {
                dialogLayout.let {
                    it()
                }
            },
            content = {

                Column(modifier = Modifier.fillMaxSize()) {

                    ScreenHeaderWithTitle(
                        title = "New Release Checklist",
                        showBackButton = true,
                        onBackClick = {
                            navigator.pop()
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(color = Color.White)
                            .verticalScroll(state = rememberScrollState())
                    ) {

                        Spacer(modifier = Modifier.height(50.dp))

                        Dropdown(
                            title = "Select Project",
                            isRequired = true,
                            isEnabled = true,
                            dropdownItems = listOfProjects.map {
                                it.fields?.projectName?.stringValue ?: ""
                            },
                            onItemsSelected = {
                            },
                            onDoneClick = {
                                coroutineScope.launch {
                                    projectName =
                                        it?.let { index -> listOfProjects.getOrNull(index)?.fields?.projectName?.stringValue }
                                            ?: projectName
                                    modalSheetState.hide()
                                }
                            },
                            showDialog = { dialog ->
                                dialogLayout = dialog
                                coroutineScope.launch {
                                    if (modalSheetState.isVisible) {
                                        modalSheetState.hide()
                                    } else {
                                        modalSheetState.show()
                                    }
                                }
                            },
                            onBackClick = {
                                coroutineScope.launch {
                                    modalSheetState.hide()
                                }
                            }
                        )


                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableEdittext(
                            title = "Branch Name",
                            placeholder = "",
                            text = branchName,
                            keyboardOptions = getKeyboardOption(),
                            isRequired = true,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            isEnabled = true,
                            isClickable = true,
                            maxCharCount = 100,
                            singleLine = true,
                            onTextChanged = { text, _ ->
                                branchName = text
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableEdittext(
                            title = "Release Version",
                            placeholder = "",
                            text = version,
                            isRequired = true,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            keyboardOptions = getKeyboardOption(),
                            isEnabled = true,
                            isClickable = true,
                            maxCharCount = 100,
                            singleLine = true,
                            onTextChanged = { text, _ ->
                                version = text
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        DateTimeComposable(
                            title = "Release Date and Time",
                            dateTimeString = releaseDateTime,
                            isEnabled = true,
                            isRequired = true,
                            onDateTimeSelected = {
                                releaseDateTime = it
                            }
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableEdittext(
                            title = "Tag",
                            placeholder = "",
                            text = tag,
                            isRequired = true,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            keyboardOptions = getKeyboardOption(),
                            isEnabled = true,
                            isClickable = true,
                            maxCharCount = 100,
                            singleLine = true,
                            onTextChanged = { text, _ ->
                                tag = text
                            }
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableRadioGroup(
                            title = "Control Center Id Checked",
                            isClickable = true,
                            isEnabled = true,
                            isRequired = true,
                            defaultSelectedIndex = 1,
                            rbList = listOf("YES", "NO"),
                            onRadioButtonChecked = {
                                controlCenterIDChecked = it.equals("YES", ignoreCase = true)
                            }
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableRadioGroup(
                            title = "Control Center Log Checked",
                            isClickable = true,
                            isEnabled = true,
                            isRequired = true,
                            defaultSelectedIndex = 1,
                            rbList = listOf("YES", "NO"),
                            onRadioButtonChecked = {
                                controlCenterLogChecked = it.equals("YES", ignoreCase = true)
                            }
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Button(modifier = Modifier
                            .padding(
                                start = 15.dp,
                                end = 15.dp,
                                top = 0.dp,
                                bottom = 5.dp
                            )
                            .fillMaxWidth()
                            .wrapContentHeight(),
                            colors = ButtonDefaults.buttonColors(colors.MAT_DARK.asColor()),
                            onClick = {

                                coroutineScope.launch(Dispatchers.IO) {

                                    if (
                                        projectName.isBlank() || branchName.isBlank() || version.isBlank() || releaseDateTime.isBlank() || tag.isBlank() || userId.isNullOrBlank() || adminId.isNullOrBlank()
                                    ){
                                        dialogShowMsgNavigateback = Triple(
                                            first = true,
                                            second = "Please fill all the required fileds.",
                                            third = false
                                        )
                                        return@launch
                                    }

                                    isLoading = true

                                    val apiResult = CommonRepository.addRelease(
                                        releases = ObjDocument(
                                            fields = Fields(
                                                projectName = StringValue(projectName),
                                                branchName = StringValue(branchName),
                                                projectId = StringValue(projectName),
                                                releaseId = StringValue(version),
                                                releaseDateTimeEpoch = StringValue(releaseDateTime),
                                                tag = StringValue(tag),
                                                baseCampIDChecked = BooleanValue(
                                                    controlCenterIDChecked
                                                ),
                                                baseCampLogCheked = BooleanValue(
                                                    controlCenterLogChecked
                                                ),
                                                userId = StringValue(userId),
                                                adminId = StringValue(adminId)
                                            ),
                                        )
                                    )

                                    dialogShowMsgNavigateback = Triple(
                                        first = true,
                                        second = if (apiResult.success == true) "Release is uploaded successfully." else "Something went wrong.\nPlease verify your internet connection and if release with same version is already not uploaded.",
                                        third = apiResult.success == true
                                    )

                                    isLoading = false
                                }

                                println("#**projectName --> ${projectName}")
                                println("#**branchName --> ${branchName}")
                                println("#**version --> ${version}")
                                println("#**releaseDateTime --> ${releaseDateTime}")
                                println("#**tag --> ${tag}")
                                println("#**controlCenterIDChecked --> ${controlCenterIDChecked}")
                                println("#**controlCenterLogChecked --> ${controlCenterLogChecked}")
                                println("#**userId --> ${userId}")
                                println("#**adminId --> ${adminId}")

                            }) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                text = "Submit Release",
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }


            }
        )

        if (isLoading) {
            AppProgressBar()
        }

        if (dialogShowMsgNavigateback.first) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AppAlertDialog(
                    modifier = Modifier.wrapContentSize(),
                    dialogTitle = "Alert",
                    dialogText = dialogShowMsgNavigateback.second,
                    showNegativeButton = false,
                    showPositiveButton = true,
                    positiveButtonText = "OK",
                    onDismissRequest = {
                        dialogShowMsgNavigateback =
                            Triple(first = false, second = "", third = false)
                        navigator.pop()
                    },
                    onConfirmation = {
                        if (dialogShowMsgNavigateback.third) {
                            navigator.pop()
                        }
                        dialogShowMsgNavigateback =
                            Triple(first = false, second = "", third = false)
                    })
            }
        }

    }
}