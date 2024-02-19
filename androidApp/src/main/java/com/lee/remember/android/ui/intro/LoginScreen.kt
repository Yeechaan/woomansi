package com.lee.remember.android.ui.intro

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
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
import com.lee.remember.android.ui.whiteColor
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.android.viewmodel.LoginViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    if (uiState.loginSuccess && uiState.fetchSuccess) {
        viewModel.resetUiState()

        navController.navigate(RememberScreen.History.name) {
            popUpTo(RememberScreen.Login.name) {
                inclusive = true
            }
        }
    }

    if (uiState.message.isNotEmpty()) {
        scope.launch {
            snackbarHostState.showSnackbar("로그인 실패")
        }
    }

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

        val passwordVisible by rememberSaveable { mutableStateOf(false) }

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

        Box {
            if (uiState.isLoading) {
                LoginLoading()
            } else {
                RememberFilledButton(text = "로그인", onClick = {
                    if (id.isEmpty() || password.value.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("이메일 또는 비밀번호는 입력해주세요.")
                        }
                        return@RememberFilledButton
                    }

                    viewModel.login(id, password.value)
                })
            }
        }
    }

}

@Composable
private fun LoginLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController(), SnackbarHostState())
}