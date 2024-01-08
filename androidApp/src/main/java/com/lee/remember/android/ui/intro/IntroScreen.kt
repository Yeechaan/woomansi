package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@Composable
fun IntroScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painterResource(id = R.drawable.img_intro), contentScale = ContentScale.FillBounds)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_splash), contentDescription = "logo",
            modifier = Modifier
                .width(88.dp)
                .padding(top = 64.dp)
        )

        Text(
            text = "우리가 만난 소중한 시절",
            modifier = Modifier.padding(top = 12.dp),
            textAlign = TextAlign.Center,
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1B),
        )

        Image(
            modifier = Modifier
                .width(142.dp)
                .padding(top = 40.dp),
            painter = painterResource(id = R.drawable.img_splash), contentDescription = "logo"
        )

        Spacer(modifier = Modifier.weight(1f))

        RememberFilledButton(text = "로그인", paddingValues = PaddingValues(top = 16.dp, bottom = 0.dp), onClick = {
            navController.navigate(RememberScreen.Login.name)
        })

        TextButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 64.dp),
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
    }
}

@Preview
@Composable
fun PreviewIntro() {
    IntroScreen(rememberNavController())
}