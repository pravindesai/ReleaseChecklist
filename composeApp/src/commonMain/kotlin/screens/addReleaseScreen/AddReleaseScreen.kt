package screens.addReleaseScreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import ui.AppProgressBar
import ui.ScreenHeaderWithTitle
import ui.form_items.ComposableEdittext
import ui.form_items.ComposableRadioGroup
import ui.form_items.Dropdown

class AddReleaseScreen : Screen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        var isLoading by remember { mutableStateOf(false) }

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
        val userId by remember { derivedStateOf {
            currentUser.fields?.userId?.stringValue ?: currentUser.fields?.userid?.stringValue
        } }
        val adminId by remember { derivedStateOf {
            currentUser.fields?.adminId?.stringValue ?: currentUser.fields?.adminid?.stringValue
        } }
        var controlCenterIDChecked by remember { mutableStateOf(false) }
        var controlCenterLogChecked by remember { mutableStateOf(false) }

        var failureDialog by remember { mutableStateOf(Pair(false, "")) }

        val modalSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = { false },
            skipHalfExpanded = false,
        )

        LaunchedEffect(listOfProjects){
            coroutineScope.launch {
                isLoading = true
                val projectListResult = CommonRepository.getAllProjectForAdmin(CommonRepository.getCurrentAdminId()?:"")
                if ((projectListResult?.success == true).not()) {
                    isLoading = false
                    val erroMsg = projectListResult?.message ?: "FAILED"
                    failureDialog = Pair(true, erroMsg)
                } else {
                    isLoading = false
                    listOfProjects = projectListResult?.data ?: emptyList()
                }
            }
        }

        ModalBottomSheetLayout(
            modifier = Modifier.fillMaxSize(),
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(0.dp),
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

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(color = Color.White)
                        .verticalScroll(state = ScrollState(0))) {

                        Spacer(modifier = Modifier.height(50.dp))

                        Dropdown(
                            title = "Select Project",
                            isRequired = true,
                            isEnabled = true,
                            dropdownItems = listOfProjects.map { it.fields?.projectName?.stringValue?:"" },
                            onItemsSelected = {
                                projectName = it?.let { index -> listOfProjects.getOrNull(index)?.fields?.projectName?.stringValue } ?: projectName
                            },
                            onDoneClick = {
                                coroutineScope.launch {
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
                            isEnabled = true,
                            isClickable = true,
                            maxCharCount = 100,
                            singleLine = true,
                            onTextChanged = { text, _ ->
                                version = text
                            }
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        ComposableEdittext(
                            title = "Release Date and Time",
                            placeholder = "",
                            text = releaseDateTime,
                            isRequired = true,
                            focusManager = focusManager,
                            focusRequester = focusRequester,
                            isEnabled = true,
                            isClickable = true,
                            maxCharCount = 100,
                            singleLine = true,
                            onTextChanged = { text, _ ->
                                releaseDateTime = text
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

        if (isLoading){
            AppProgressBar()
        }

    }
}