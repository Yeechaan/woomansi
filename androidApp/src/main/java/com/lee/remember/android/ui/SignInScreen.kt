package com.lee.remember.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.GreetingKtor
import com.lee.remember.android.GreetingView
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.userId
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.request.RegisterRequest
import com.lee.remember.request.SignInRequest
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        GreetingView(text)

        TextField(
            value = id, onValueChange = { id = it },
            label = {
                Text("email", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        TextField(
            value = password, onValueChange = { password = it },
            label = {
                Text("password", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {
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
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            Text(
                text = "회원가입",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

    }

}

@Preview
@Composable
fun PreviewSignInScreen() {
    SignInScreen(rememberNavController())
}