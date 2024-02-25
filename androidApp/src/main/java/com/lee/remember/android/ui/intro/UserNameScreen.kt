package com.lee.remember.android.ui.intro

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.ui.common.RememberTopAppBar
import com.lee.remember.android.ui.friend.lightColor
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.android.viewmodel.IntroViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameScreen(
    navController: NavHostController,
    viewModel: IntroViewModel = koinViewModel(),
) {
    val uiState = viewModel.signUpUiState.collectAsState()

    if (uiState.value.signupResult) {
        navController.navigate(RememberScreen.SelectContact.name) {
            popUpTo(RememberScreen.Intro.name)
        }
    }
    if (uiState.value.loading) {

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
            .background(lightColor)
            .padding(bottom = 32.dp)
            .verticalScroll(scrollState),
    ) {
        RememberTopAppBar(navHostController = navController, title = "닉네임")

        var nickname by remember { mutableStateOf("") }

        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            text = "사용하실 닉네임을\n입력해주세요.", style = getTextStyle(textStyle = RememberTextStyle.HEAD_3), textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = nickname, onValueChange = { nickname = it },
            label = { RememberTextField.label(text = "닉네임") },
            placeholder = { RememberTextField.placeHolder(text = "호랑이") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = { viewModel.updateUserName(nickname) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
            shape = RoundedCornerShape(size = 100.dp),
        ) {
            Text(
                text = "완료하기",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0x94000000)),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewUserNameScreen() {
    UserNameScreen(rememberNavController())
}