package com.lee.remember.android.ui.intro

import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.lee.remember.GreetingKtor
import com.lee.remember.android.GreetingView
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.fontColorPoint
import com.lee.remember.android.ui.fontHintColor
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.ui.whiteColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.android.utils.rememberTextFieldStyle
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.User
import com.lee.remember.request.LoginRequest
import com.lee.remember.request.RegisterRequest
import com.lee.remember.request.RegisterResponse
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavHostController) {

    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

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

        var id by remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val passwordConfirm = remember { mutableStateOf("") }
        val isPasswordError = remember { mutableStateOf(false) }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        GreetingView(text)

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = id, onValueChange = { id = it },
            label = {
                Text("이메일", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            placeholder = {
                Text("이메일 입력", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(fontHintColor))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            singleLine = true,
            colors = rememberTextFieldStyle()
        )

        OutlinedTextField(
            value = password.value, onValueChange = { password.value = it },
            label = {
                Text("비밀번호", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            placeholder = {
                Text("비밀번호 입력", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(fontHintColor))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = rememberTextFieldStyle()
        )

        OutlinedTextField(
            value = passwordConfirm.value,
            onValueChange = {
                isPasswordError.value = password.value != it

                passwordConfirm.value = it
            },
            label = {
                Text("비밀번호 확인", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            placeholder = {
                Text("비밀번호 확인", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(fontHintColor))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            isError = isPasswordError.value,
            supportingText = {
                if (isPasswordError.value) {
                    Text("입력하신 비밀번호가 일치하지 않습니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFFD59519)))
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                errorBorderColor = Color(0xFFB3661E),
                errorLabelColor = Color(0xFFB3661E),
                focusedBorderColor = fontColorPoint,
                focusedLabelColor = fontColorPoint,
                cursorColor = fontColorPoint
            ),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        val context = LocalContext.current

        Button(
            onClick = {
                if (password.value != passwordConfirm.value) {
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (id.isEmpty() || password.value.isEmpty() || passwordConfirm.value.isEmpty()) {
                    Toast.makeText(context, "이메일 또는 비밀번호는 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    text = try {
                        val signInRequest = RegisterRequest(id, "", password.value, "")
                        val response = GreetingKtor().register(signInRequest)

                        if (response != null) {
                            saveUser(response, password.value)

                            val signInResponse = GreetingKtor().login(LoginRequest(id, password.value))
                            if (signInResponse != null) {
                                accessToken = signInResponse.result?.jwtToken ?: ""

                                navController.navigate(RememberScreen.UserName.name)
                            } else {
                                "에러"
                            }

                            response.toString()
                        } else {
                            "에러"
                        }
                    } catch (e: Exception) {
                        e.localizedMessage ?: "error"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
            shape = RoundedCornerShape(size = 100.dp),
        ) {
            Text(
                text = "가입하기",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

    }
}

suspend fun saveUser(signResponse: RegisterResponse, password: String) {
    signResponse.result?.let {
        val user = User().apply { this.email = it.email; this.password = password; this.userId = it.id }
        UserDao().setUser(user)
    }
}

@Preview
@Composable
fun PreviewSignInScreen() {
    SignInScreen(rememberNavController())
}