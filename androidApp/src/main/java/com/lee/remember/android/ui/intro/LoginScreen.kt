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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.lee.remember.android.GreetingView
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.whiteColor
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.local.dao.UserDao
import com.lee.remember.local.model.UserRealm
import com.lee.remember.remote.AuthApi
import com.lee.remember.remote.request.LoginRequest
import com.lee.remember.repository.AuthRepository
import com.lee.remember.repository.FriendRepository
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {
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
            title = { Text("로그인", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
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
        val isPasswordError = remember { mutableStateOf(false) }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        GreetingView(text)

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = id, onValueChange = { id = it },
            label = { RememberTextField.label(text = "이메일") },
            placeholder = { RememberTextField.placeHolder(text = "이메일 입력") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

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
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        RememberFilledButton(text = "로그인", onClick = {
            scope.launch {
                if (id.isEmpty() || password.value.isEmpty()) {
                    snackbarHostState.showSnackbar("이메일 또는 비밀번호는 입력해주세요.")
                    return@launch
                }

                val result = AuthRepository().login(id, password.value)
                if (result.isSuccess) {
                    UserRepository().fetchUser()
                    FriendRepository().fetchFriends()

                    navController.navigate(RememberScreen.History.name) {
                        popUpTo(RememberScreen.Login.name) {
                            inclusive = true
                        }
                    }
                } else {
                    snackbarHostState.showSnackbar("로그인 실패")
                }
            }
        })

//        Button(
//            onClick = {
//                if (id.isEmpty() || password.value.isEmpty()) {
//                    Toast.makeText(context, "이메일 또는 비밀번호는 입력해주세요.", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                scope.launch {
//                    val result = AuthRepository().login(id, password.value)
//                    if (result.isSuccess) {
//                        navController.navigate(RememberScreen.History.name) {
//                            popUpTo(RememberScreen.Login.name) {
//                                inclusive = true
//                            }
//                        }
//                    } else {
//                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
//            shape = RoundedCornerShape(size = 100.dp),
//        ) {
//            Text(
//                text = "로그인",
//                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White),
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//        }

    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController(), SnackbarHostState())
}