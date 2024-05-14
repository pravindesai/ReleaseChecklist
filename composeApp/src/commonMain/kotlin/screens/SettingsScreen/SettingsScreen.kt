package screens.SettingsScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.MAT_DARK
import colors.asColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import repository.CommonRepository
import repository.models.ApiResult
import repository.models.data.ObjAdmin
import repository.models.data.ObjDocument
import repository.models.data.ObjUser
import screens.mainscreen.UserSelectionScreen
import ui.AddProjectDialog
import ui.AddUserDialog
import ui.AppAlertDialog
import ui.AppProgressBar
import ui.PlainTextTile

class SettingsScreen : Screen {

    @Composable
    operator fun invoke() = Content()

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val tabNavigator = LocalNavigator.currentOrThrow
        var isLoading by remember { mutableStateOf(false) }
        val currentUser by remember { mutableStateOf(CommonRepository.getLoggedInUser()) }
        val isAdmin by remember { derivedStateOf { currentUser is ObjAdmin } }
        val admin by remember { derivedStateOf { currentUser as? ObjAdmin } }
        val user by remember { derivedStateOf { currentUser as? ObjUser } }
        val haveSpecialRights by remember {
            derivedStateOf {
                admin?.fields?.specialRights?.booleanValue ?: false
            }
        }

        var showAddUserDialog by remember { mutableStateOf(false) }
        var showAddAdminDialog by remember { mutableStateOf(false) }
        var showAddProjectDialog by remember { mutableStateOf(false) }

        var apiDialogMessage by remember { mutableStateOf("") }
        var showAPIErrorDialog by remember { mutableStateOf(false) }

        var apiSuccessDialog by remember { mutableStateOf<Pair<Boolean, String?>>(Pair(false, null)) }

        var tileOne by remember { mutableStateOf(false) }
        var tileTwo by remember { mutableStateOf(false) }
        var tileThree by remember { mutableStateOf(false) }
        var tileFour by remember { mutableStateOf(false) }

        LaunchedEffect(Unit){
            coroutineScope.launch {
                delay(100)
                tileOne   = true
                delay(100)
                tileTwo   = true
                delay(100)
                tileThree   = true
                delay(100)
                tileFour   = true
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().animateContentSize()
                .verticalScroll(state = rememberScrollState(), enabled = true),
        ) {

            Spacer(modifier = Modifier.weight(1f))

            AnimatedContent(
                targetState = tileOne,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(200),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                }
            ) { targetState ->
                PlainTextTile(
                    modifier = Modifier.background(color = if (targetState) colors.LIGHT_GRAY.asColor() else Color.Transparent).padding(top = 50.dp, bottom = 50.dp),
                    textColor = colors.MAT_WHITE.asColor(),
                    text = "Add User",
                    onClick = {
                        showAddUserDialog = true
                    }
                )
            }


            Spacer(modifier = Modifier.height(1.dp))


            AnimatedContent(
                targetState = tileTwo,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(200),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                }
            ) { targetState ->
                PlainTextTile(
                    modifier = Modifier.background(color = if (targetState) colors.LIGHT_GRAY.asColor() else Color.Transparent).padding(top = 50.dp, bottom = 50.dp),
                    textColor = colors.MAT_WHITE.asColor(),
                    text = "Add Project",
                    onClick = {
                        showAddProjectDialog = true
                    }
                )
            }


            Spacer(modifier = Modifier.height(1.dp))


            if (haveSpecialRights) {

                AnimatedContent(
                    targetState = tileThree,
                    transitionSpec = {
                        slideInHorizontally(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessLow,
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                visibilityThreshold = IntOffset.VisibilityThreshold
                            ),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(200),
                                    targetOffsetX = { fullWidth -> -fullWidth }
                                )
                    }
                ) { targetState ->
                    PlainTextTile(
                        modifier = Modifier.background(color = if (targetState) colors.LIGHT_GRAY.asColor() else Color.Transparent).padding(top = 50.dp, bottom = 50.dp),
                        textColor = colors.MAT_WHITE.asColor(),
                        text = "Add Admin",
                        onClick = {
                            showAddAdminDialog = true
                        }
                    )
                }


                Spacer(modifier = Modifier.height(1.dp))

            }


            AnimatedContent(
                targetState = tileFour,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = spring(
                            stiffness = Spring.StiffnessLow,
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        ),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith
                            slideOutHorizontally(
                                animationSpec = tween(200),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            )
                }
            ) { targetState ->
                PlainTextTile(
                    modifier = Modifier.background(color = if (targetState) colors.RED.asColor() else Color.Transparent).padding(top = 50.dp, bottom = 50.dp),
                    textColor = colors.MAT_WHITE.asColor(),
                    text = "Sign Out",
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val isSignedOut = signOut()
                            if (isSignedOut) {
                                tabNavigator.parent?.replaceAll(UserSelectionScreen())
                            }
                            isLoading = false
                        }

                    }
                )
            }

            Spacer(modifier = Modifier.height(50.dp).background(color = colors.LIGHT_GRAY.asColor()))

        }



        if (showAddUserDialog) {
            AddUserDialog(onDismissClick = {
                showAddUserDialog = false
            }, onDoneClick = { userId, userPass, _ ->
                showAddUserDialog = false
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = addUser(userId, userPass)
                    isLoading = false
                    if ((result.success==true).not()){
                        apiDialogMessage = result.message?:"FAILED"
                        showAPIErrorDialog = true
                    }else{
                        apiSuccessDialog = Pair(true, "User added successfully.")
                    }
                }
            })
        }

        if (showAddAdminDialog) {
            AddUserDialog(addAsAdmin = true, onDismissClick = {
                showAddAdminDialog = false
            }, onDoneClick = { userId, userPass, orgName ->
                showAddAdminDialog = false
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = addAdmin(userId, userPass, orgName)
                    isLoading = false
                    if ((result.success==true).not()){
                        apiDialogMessage = result.message?:"FAILED"
                        showAPIErrorDialog = true
                    }else{
                        apiSuccessDialog = Pair(true, "Admin added successfully.")
                    }
                }
            })
        }

        if (showAddProjectDialog) {
            AddProjectDialog(onDismissClick = {
                showAddProjectDialog = false
            }, onDoneClick = { projectName ->
                showAddProjectDialog = false
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val result = addProject(projectName)
                    isLoading = false
                    if ((result.success==true).not()){
                        apiDialogMessage = result.message?:"FAILED"
                        showAPIErrorDialog = true
                    }else{
                        apiSuccessDialog = Pair(true, "Project added successfully.")
                    }
                }
            })
        }

        if (isLoading) {
            AppProgressBar()
        }
        if (showAPIErrorDialog) {
            Box(
                modifier = Modifier.fillMaxSize().clickable {
                    showAPIErrorDialog = false
                },
                contentAlignment = Alignment.Center
            ) {
                AppAlertDialog(
                    modifier = Modifier.wrapContentSize(),
                    showPositiveButton = false,
                    dialogTitle = "Alert",
                    dialogText = apiDialogMessage,
                    onDismissRequest = {
                        showAPIErrorDialog = false
                    },
                    onConfirmation = {
                        showAPIErrorDialog = false
                    })
            }
        }

        if (apiSuccessDialog.first){
            Box(
                modifier = Modifier.fillMaxSize().clickable {
                    apiSuccessDialog = Pair(false, null)
                },
                contentAlignment = Alignment.Center
            ){
                AppAlertDialog(
                    modifier = Modifier.wrapContentSize(),
                    showPositiveButton = false,
                    dialogTitle = "Alert",
                    dialogText = apiSuccessDialog.second?:"",
                    onDismissRequest = {
                        apiSuccessDialog = Pair(false, null)
                    },
                    onConfirmation = {
                        apiSuccessDialog = Pair(false, null)
                    })
            }
        }
    }

    private suspend fun signOut(): Boolean {
        return CommonRepository.signOut()
    }

}

suspend fun addUser(userId: String, userPass: String):ApiResult<ObjUser>{
    return CommonRepository.addUser(userId = userId, userPass = userPass)
}

suspend fun addProject(projectName:String):ApiResult<ObjDocument>{
    return CommonRepository.addProject(projectName = projectName)
}

suspend fun addAdmin(adminId: String, adminPass:String, org:String?):ApiResult<ObjDocument>{
    return CommonRepository.addAdmin(adminId = adminId, adminPass = adminPass, org = org)
}