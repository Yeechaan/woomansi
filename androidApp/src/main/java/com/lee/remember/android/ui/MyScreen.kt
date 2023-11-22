package com.lee.remember.android.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.GreetingKtor
import com.lee.remember.android.GreetingView
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.userId
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.request.RegisterRequest
import com.lee.remember.request.SignInRequest
import kotlinx.coroutines.launch

@Composable
fun MyScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(bottom = 32.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {
        HeadingLogoScreen()

        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirm by remember { mutableStateOf("") }
        var nickname by remember { mutableStateOf("") }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        GreetingView(text)

        Text("내 정보", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5), modifier = Modifier.padding(top = 24.dp, start = 16.dp))

        OutlinedTextField(
            value = id, onValueChange = { id = it },
            label = {
                Text("이메일", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_4B),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = {
                Text("비밀번호", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_4B),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = passwordConfirm, onValueChange = { passwordConfirm = it },
            label = {
                Text("비밀번호 확인", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_4B),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        OutlinedTextField(
            value = nickname, onValueChange = { nickname = it },
            label = {
                Text("닉네임", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_4B),
            modifier = Modifier
                .padding(top = 16.dp).padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        val context = LocalContext.current

        Button(
            onClick = {
                if (password != passwordConfirm) {
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    text = try {
                        val signInRequest = RegisterRequest(id, "name", password, "")
                        val response = GreetingKtor().register(signInRequest)

                        if (response != null) {
                            val signInResponse = GreetingKtor().signIn(SignInRequest(id, password))
                            if (signInResponse != null) {
                                userId = signInResponse.result?.userId ?: -1
                                accessToken = signInResponse.result?.jwtToken ?: ""
                                navController.navigate(RememberScreen.SelectContact.name)
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
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            Text(
                text = "회원가입",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

    }
}

@Composable
fun HeadingLogoScreen() {
    Column(
        Modifier
            .shadow(elevation = 1.dp, spotColor = Color(0x36263E2B), ambientColor = Color(0x36263E2B))
            .shadow(elevation = 1.dp, spotColor = Color(0x33444444), ambientColor = Color(0x33444444))
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "camera_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(pointColor)
        )

        Text("우만시", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5), modifier = Modifier.padding(top = 16.dp, bottom = 24.dp))

        Divider(color = pointColor, thickness = 8.dp)
    }
}

@Preview
@Composable
fun PreviewMyScreen() {
    MyScreen(rememberNavController())
}