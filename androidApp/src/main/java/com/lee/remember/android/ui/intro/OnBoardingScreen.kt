package com.lee.remember.android.ui.intro

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {

        val items = listOf(
            Triple(R.drawable.img_onboard_1, "소중한 인연", "내가 관리하고픈 소중한 인연들만 선택해요"),
            Triple(R.drawable.img_onboard_2, "아름다운 기억", "그들과의 기억을 소중히 간직해요"),
            Triple(R.drawable.img_onboard_3, "기억 공유", "그들과의 기억을 떠올리고 공유해요")
        )

        val pagerState = rememberPagerState(pageCount = { items.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val item = items[page]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
//                    .clip(GenericShape { size, _ ->
//                        lineTo(size.width, 0f)
//                        lineTo(size.width, Float.MAX_VALUE)
//                        lineTo(0f, Float.MAX_VALUE)
//                    })
                    .shadow(10.dp)
                    .background(Color.White)
            ) {
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(270.dp)
                            .padding(top = 80.dp)
                            .padding(horizontal = 40.dp),
                        painter = painterResource(id = item.first),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(top = 80.dp),
                        text = item.second, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)
                    )

                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 40.dp),
                        text = item.third, style = getTextStyle(textStyle = RememberTextStyle.BODY_2)
                    )
                }
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFFD59519) else Color(0xFFD8D8D8)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }

        if (pagerState.currentPage == 2) {
            Button(
                onClick = {
                    navController.navigate(RememberScreen.Intro.name) {
                        popUpTo(RememberScreen.OnBoarding.name) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
                shape = RoundedCornerShape(size = 100.dp),
            ) {
                Text(
                    text = "시작하기",
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White)
                )
            }
        } else {
            TextButton(
                onClick = {
                    navController.navigate(RememberScreen.Intro.name) {
                        popUpTo(RememberScreen.OnBoarding.name) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
            ) {
                Text(text = "SKIP", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFFD59519)))
            }
        }
    }
}

@Preview
@Composable
fun PreviewOnBoardingScreen() {
    OnBoardingScreen(navController = rememberNavController())
}