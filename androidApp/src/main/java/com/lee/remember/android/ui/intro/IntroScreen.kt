package com.lee.remember.android.ui.intro

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.repository.UserRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@Composable
fun IntroScreen(navController: NavHostController) {
    val activity = (LocalContext.current as Activity)
    val status = ContextCompat.checkSelfPermission(activity.applicationContext, "android.permission.READ_CONTACTS")
    if (status == PackageManager.PERMISSION_GRANTED) {
        Log.d("test", "permission granted")
    } else {
        ActivityCompat.requestPermissions(activity, arrayOf("android.permission.READ_CONTACTS"), 100)
        Log.d("test", "permission denied")
    }


    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painterResource(id = R.drawable.img_intro), contentScale = ContentScale.FillBounds)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_main),
                contentDescription = "logo",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
//                .width(88.dp)
                    .padding(top = 64.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.logo_splash),
                contentDescription = "logo",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .width(88.dp)
                    .padding(top = 12.dp)
            )

            Text(
                text = "우리가 만난 소중한 시절",
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center,
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(Color.White),
            )

            Image(
                modifier = Modifier
                    .width(142.dp)
                    .padding(top = 40.dp),
                painter = painterResource(id = R.drawable.logo_intro), contentDescription = "logo",
            )
        }
//        Spacer(modifier = Modifier.weight(1f))

        RememberFilledButton(text = "로그인", verticalPaddingValues = PaddingValues(top = 16.dp, bottom = 0.dp), onClick = {
            navController.navigate(RememberScreen.Login.name)
        })

        TextButton(
            modifier = Modifier
                .padding(top = 8.dp),
//                .padding(bottom = 64.dp),
                onClick = { navController.navigate(RememberScreen.Terms.name) }) {
            Text(
                text = "새로 시작하기",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color.White),
                modifier = Modifier.drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height - 1.sp.toPx()
                    drawLine(
                        color = Color.White,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                }
            )
        }

        val scope = rememberCoroutineScope()
        TextButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 64.dp),
            onClick = {
                scope.launch {
                    UserRepository().addTestUser()
                    scope.cancel()
                }
                navController.navigate(RememberScreen.SelectContact.name)
            }) {
            Text(
                text = "로그인 없이 사용해보기",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color.White),
                modifier = Modifier.drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height - 1.sp.toPx()
                    drawLine(
                        color = Color.White,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewIntro() {
    IntroScreen(rememberNavController())
}