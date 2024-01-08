package com.lee.remember.android.ui.intro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.fontHintColor
import com.lee.remember.android.ui.whiteColor
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.UserRealm
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.request.SignupRequest
import com.lee.remember.remote.request.SignupResponse
import com.lee.remember.repository.AuthRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController) {

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

        TopAppBar(
            modifier = Modifier.shadow(elevation = 10.dp),
            title = { Text("회원가입", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
        )

        var email by remember { mutableStateOf("") }
        var emailCode by remember { mutableStateOf("") }
        var isValid by remember { mutableStateOf(true) }

        val password = remember { mutableStateOf("") }
        val passwordConfirm = remember { mutableStateOf("") }
        val isPasswordError = remember { mutableStateOf(false) }

        // Ktor Test
        val scope = rememberCoroutineScope()

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        val isEmailConfirmed = remember { mutableStateOf(false) }
        val openAlertDialog = remember { mutableStateOf(false) }
        if (openAlertDialog.value) {
            EmailConfirmDialog(
                emailCode = emailCode,
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    isEmailConfirmed.value = it
                    openAlertDialog.value = false
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = email, onValueChange = { email = it },
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
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            val emailConfirmColor = if (isEmailConfirmed.value) Color(0xFFF2BE2F) else fontHintColor
            TextButton(
                onClick = {
                    if (isValid) {
                        openAlertDialog.value = true

                        scope.launch {
                            val response = AuthApi().sendEmailCode(email)
                            emailCode = response?.result?.code ?: ""

                            if (response != null && response.resultCode == "SUCCESS") {
//                                openAlertDialog.value = true
                            }
                        }
                    }
                },
                modifier = Modifier.border(width = 3.dp, color = emailConfirmColor, shape = RoundedCornerShape(size = 4.dp))
            ) {
                Text(
                    "인증",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(emailConfirmColor),
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
                .padding(top = 4.dp)
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
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        val context = LocalContext.current

        Button(
            onClick = {
                if (password.value != passwordConfirm.value) {
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (email.isEmpty() || password.value.isEmpty() || passwordConfirm.value.isEmpty()) {
                    Toast.makeText(context, "이메일 또는 비밀번호는 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        val result = AuthRepository().signUp(email, password.value)
                        if (result.isSuccess) {
                            navController.navigate(RememberScreen.UserName.name) {
                                popUpTo(RememberScreen.UserName.name) {
                                    inclusive = true
                                }
                            }
                        } else {
                            Toast.makeText(context, "Internal Server Error", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.localizedMessage ?: "error"
                    }

                    scope.cancel()
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
    SignInScreen(rememberNavController())
}