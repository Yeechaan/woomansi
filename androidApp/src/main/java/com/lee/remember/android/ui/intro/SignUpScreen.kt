package com.lee.remember.android.ui.intro

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.ui.RememberScreen
import com.lee.remember.android.ui.common.RememberTopAppBar
import com.lee.remember.android.ui.memory.fontHintColor
import com.lee.remember.android.ui.friend.whiteColor
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.android.viewmodel.IntroViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController, snackbarHostState: SnackbarHostState,
    viewModel: IntroViewModel = koinViewModel(),
) {
    val uiState by viewModel.signUpUiState.collectAsState()
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(false) }
    loading = viewModel.signUpUiState.value.loading

    var emailCode by remember { mutableStateOf("") }
    val isEmailConfirmed = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }
    if (openAlertDialog.value) {
        EmailConfirmDialog(
            emailCode = emailCode,
            onDismissRequest = {
                openAlertDialog.value = false
                viewModel.resetSignUpUiState()
            },
            onConfirmation = {
                isEmailConfirmed.value = it
                openAlertDialog.value = false
                viewModel.resetSignUpUiState()
            }
        )
    }

    // Todo 맞네 초기화 로직이 필요하네.. 매번 그렇게 해야하나?
    if (uiState.emailCodeResult.isNotEmpty()) {
        emailCode = uiState.emailCodeResult
        openAlertDialog.value = true
    }

    if (uiState.signupResult) {
        // Todo 꼭 이렇게 처리해야 하나?
        viewModel.resetSignUpUiState()

        navController.navigate(RememberScreen.UserName.name) {
            popUpTo(RememberScreen.SignUp.name) {
                inclusive = true
            }
        }
    }

    LaunchedEffect(uiState.message) {
        if (uiState.message.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.message)
            viewModel.resetSignUpUiState()
        }
    }


    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
//    LaunchedEffect(key1 = imeState.value) {
//        if (imeState.value) {
//            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
//        }
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteColor)
            .verticalScroll(scrollState),
    ) {

        RememberTopAppBar(navHostController = navController, title = "회원가입")

        var email by remember { mutableStateOf("") }
//        var emailCode by remember { mutableStateOf("") }
        var isValid by remember { mutableStateOf(true) }

        val password = remember { mutableStateOf("") }
        val passwordConfirm = remember { mutableStateOf("") }
        val isPasswordError = remember { mutableStateOf(false) }


        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = email, onValueChange = { email = it },
//            readOnly = isEmailConfirmed.value,
            label = { RememberTextField.label(text = "이메일") },
            placeholder = { RememberTextField.placeHolder(text = "이메일 입력") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            isError = !isValid && email.isNotEmpty(),
            supportingText = {
                val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
                isValid = email.matches(emailRegex.toRegex())

                if (!isValid && email.isNotEmpty()) {
                    Text("이메일 형식이 올바르지 않습니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFFB3661E)))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp),
        )

        val emailConfirmedText = if (isEmailConfirmed.value) "인증완료" else "인증"
        val emailConfirmedColor = if (isEmailConfirmed.value) Color(0xFFF2BE2F) else fontHintColor
        Button(
            onClick = { viewModel.sendEmailCode(email) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 4.dp),
            border = BorderStroke(1.dp, emailConfirmedColor)
        ) {
            Box {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFFF2BE2F),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    return@Button
                }

                Text(
                    text = emailConfirmedText,
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(emailConfirmedColor),
                )
            }
        }

        OutlinedTextField(
            value = password.value, onValueChange = { password.value = it },
            label = { RememberTextField.label(text = "비밀번호") },
            placeholder = { RememberTextField.placeHolder(text = "비밀번호 입력") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        OutlinedTextField(
            value = passwordConfirm.value,
            onValueChange = {
                isPasswordError.value = password.value != it

                passwordConfirm.value = it
            },
            label = { RememberTextField.label(text = "비밀번호 확인") },
            placeholder = { RememberTextField.placeHolder(text = "비밀번호 확인") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            isError = isPasswordError.value,
            supportingText = {
                if (isPasswordError.value) {
                    Text("입력하신 비밀번호가 일치하지 않습니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFFB3661E)))
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )


        Button(
            onClick = {
                scope.launch {
                    if (password.value != passwordConfirm.value) {
                        snackbarHostState.showSnackbar("비밀번호가 일치하지 않습니다.")
                        return@launch
                    }

                    if (email.isEmpty() || password.value.isEmpty() || passwordConfirm.value.isEmpty()) {
                        snackbarHostState.showSnackbar("이메일 또는 비밀번호는 입력해주세요.")
                        return@launch
                    }

                    // Todo compose state 처리
                    viewModel.signUp(email, password.value)
                }
            },
            // Todo 버튼 활성화 정책 설정
//            enabled = isValid && isEmailConfirmed.value && password.value.isNotEmpty() && (password.value == passwordConfirm.value),
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
            shape = RoundedCornerShape(size = 100.dp),
        ) {
            Text(
                text = "가입하기",
                modifier = Modifier.padding(vertical = 2.dp),
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0x94000000))
            )
        }

    }
}

@Preview
@Composable
fun PreviewSignInScreen() {
    SignUpScreen(rememberNavController(), SnackbarHostState())
}