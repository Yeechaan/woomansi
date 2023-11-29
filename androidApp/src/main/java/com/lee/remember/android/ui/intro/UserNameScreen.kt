package com.lee.remember.android.ui.intro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.lee.remember.GreetingKtor
import com.lee.remember.android.GreetingView
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.fontHintColor
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.request.UserInfoRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightColor)
            .padding(bottom = 32.dp),
    ) {

        TopAppBar(
            modifier = Modifier.shadow(elevation = 10.dp),
            title = { Text("닉네임", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
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

        var nickname by remember { mutableStateOf("") }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

//        GreetingView(text)

        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            text = "사용하실 닉네임을\n입력해주세요.", style = getTextStyle(textStyle = RememberTextStyle.HEAD_3), textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = nickname, onValueChange = { nickname = it },
            label = {
                Text("넥네임", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            placeholder = {
                Text("호랑이", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(fontHintColor))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        val context = LocalContext.current

        Button(
            onClick = {
                if (nickname.isEmpty()) {
                    Toast.makeText(context, "이메일 또는 비밀번호는 입력해주세요.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    text = try {
                        val userInfoRequest = UserInfoRequest(nickname, "")
                        val response = GreetingKtor().setUser(accessToken, userInfoRequest)

                        if (response != null) {
                            navController.navigate(RememberScreen.SelectContact.name)

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
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
            shape = RoundedCornerShape(size = 100.dp),
        ) {
            Text(
                text = "시작하기",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

//        Button(
//            onClick = {
//                navController.navigate(RememberScreen.SelectContact.name)
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = lightColor),
//            shape = RoundedCornerShape(size = 100.dp),
//        ) {
//            Text(
//                text = "건너뛰기",
//                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFFF2BE2F)),
//                modifier = Modifier.padding(vertical = 2.dp)
//            )
//        }
    }
}

@Preview
@Composable
fun PreviewUserNameScreen() {
    UserNameScreen(rememberNavController())
}