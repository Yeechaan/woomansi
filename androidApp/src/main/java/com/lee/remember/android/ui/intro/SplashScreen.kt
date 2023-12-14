package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.local.dao.UserDao
import com.lee.remember.remote.AuthApi
import com.lee.remember.request.LoginRequest
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(lightColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.width(100.dp),
            painter = painterResource(id = R.drawable.ic_appbar), contentDescription = "logo"
        )

        val scope = rememberCoroutineScope()
        scope.launch {
            val user = UserDao().getUser()
//            delay(1000)

            if (user != null) {
                val loginResponse = AuthApi().login(LoginRequest(user.email, user.password))
                if (loginResponse != null) {
                    accessToken = loginResponse.result?.jwtToken ?: ""

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
                navController.navigate(RememberScreen.OnBoarding.name) {
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