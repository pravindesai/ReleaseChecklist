package screens.loginscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import colors.asColor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import repository.CommonRepository
import screens.dashboardscreen.DashboardScreen
import strings.UserType
import ui.AppAlertDialog
import ui.AppProgressBar

class LoginScreen(val userType: UserType) : Screen {
    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var isSignInFailed by remember { mutableStateOf(false) }

        var showErrorForId by remember { mutableStateOf(false) }
        var showErrorForPassword by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .wrapContentHeight()
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colors.YELLOW_TINT.asColor(),
                )

            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    OutlinedTextField(
                        value = id,
                        isError = showErrorForId,
                        onValueChange = {
                            showErrorForId = false
                            id = it
                        },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        singleLine = true,
                        label = {
                            Text("Enter Id")
                        },
                        textStyle = TextStyle(
                            color = colors.MAT_DARK.asColor(),
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.PRIMARY_ORANGE.asColor(),
                            unfocusedBorderColor = colors.MAT_DARK.asColor(),
                            focusedLabelColor = colors.PRIMARY_ORANGE.asColor(),
                            unfocusedLabelColor = colors.MAT_DARK.asColor()
                        ),
                        supportingText = {
                            if (showErrorForId) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Please enter valid ID",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = password,
                        isError = showErrorForPassword,
                        onValueChange = {
                            showErrorForPassword = false
                            password = it
                        },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        singleLine = true,
                        label = {
                            Text("Enter Password")
                        },
                        textStyle = TextStyle(
                            color = colors.MAT_DARK.asColor(),
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardActions = KeyboardActions(onDone = {

                            if (id.isBlank()){
                                showErrorForId = true
                                return@KeyboardActions
                            }
                            if (password.isBlank()){
                                showErrorForPassword = true
                                return@KeyboardActions
                            }

                            coroutineScope.launch {
                                isLoading = true
                                val isSignInSuccess = async { trySignIn(id, password, userType) }.await()
                                isLoading = false
                                if (isSignInSuccess){
                                    gotoDashboard(userType, navigator)
                                }else{
                                    isSignInFailed = true
                                }
                            }
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.PRIMARY_ORANGE.asColor(),
                            unfocusedBorderColor = colors.MAT_DARK.asColor(),
                            focusedLabelColor = colors.PRIMARY_ORANGE.asColor(),
                            unfocusedLabelColor = colors.MAT_DARK.asColor()
                        ),
                        supportingText = {
                            if (showErrorForPassword) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Please enter valid Password",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedButton(
                        shape = RoundedCornerShape(10),
                        onClick = {
                            if (id.isBlank()){
                                showErrorForId = true
                                return@OutlinedButton
                            }
                            if (password.isBlank()){
                                showErrorForPassword = true
                                return@OutlinedButton
                            }

                            coroutineScope.launch {
                                isLoading = true
                                val isSignInSuccess = async { trySignIn(id, password, userType) }.await()
                                isLoading = false
                                if (isSignInSuccess){
                                    gotoDashboard(userType, navigator)
                                }else{
                                    isSignInFailed = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        border = BorderStroke(1.dp, color = colors.MAT_DARK.asColor()),
                        content = {
                            Text(
                                color = colors.MAT_DARK.asColor(),
                                text = "Sign In",
                                modifier = Modifier.wrapContentSize(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

        }

        if (isLoading){
            AppProgressBar()
        }

        if (isSignInFailed){
            Box(
                modifier = Modifier.fillMaxSize().clickable {
                    isSignInFailed = false
                },
                contentAlignment = Alignment.Center
            ){
                AppAlertDialog(
                    modifier = Modifier.wrapContentSize(),
                    showPositiveButton = false,
                    dialogTitle = "Sign In Failed",
                    dialogText = "Please verify your Internet Connectivity and Login Credentials.\nIf issue still persists please contact Admin.",
                    onDismissRequest = {
                        isSignInFailed = false
                    },
                    onConfirmation = {
                        isSignInFailed = false
                    })
            }
        }

    }

    private suspend fun trySignIn(userId:String, userPassword:String, userType: UserType):Boolean{
        return CommonRepository.trySignIn(userId, userPassword, userType)
    }

    private fun gotoDashboard(userType: UserType, navigator: Navigator){
        navigator.push(DashboardScreen(userType = userType))
    }
}