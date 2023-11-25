package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.GreetingKtor
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.local.dao.UserDao
import com.lee.remember.request.LoginRequest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightColor)
            .padding(bottom = 32.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(top = 126.dp),
            text = "우리가 만났던 소중한 시절", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(Color(0xFF40454E))
        )

        Image(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(top = 12.dp),
            painter = painterResource(id = R.drawable.logo_splash), contentDescription = "logo"
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 46.dp),
            painter = painterResource(id = R.drawable.img_splash), contentDescription = "logo"
        )

        val scope = rememberCoroutineScope()
        scope.launch {
            val user = UserDao().getUser()
//            delay(1000)

            if (user != null) {
                val loginResponse = GreetingKtor().login(LoginRequest(user.email, user.password))
                if (loginResponse != null) {
                    accessToken = loginResponse.result?.jwtToken ?: ""

                    scope.cancel()
                    navController.navigate(RememberScreen.History.name) {
                        popUpTo(RememberScreen.Splash.name) {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate(RememberScreen.Login.name) {
                        popUpTo(RememberScreen.Splash.name) {
                            inclusive = true
                        }
                    }
                }
            } else {
                navController.navigate(RememberScreen.Intro.name) {
                    popUpTo(RememberScreen.Splash.name) {
                        inclusive = true
                    }
                }
            }

            scope.cancel()
        }

        // Todo 사용자 정보 api 호출 또는 db에서 읽기, 없으면 intro 이동
    }
}

@Preview
@Composable
fun PreviewSplash() {
    SplashScreen(rememberNavController())
}