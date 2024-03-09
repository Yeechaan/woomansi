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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.ui.RememberScreen
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberOutlinedButton
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
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) { page ->
            val item = items[page]

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .padding(horizontal = 40.dp),
                        painter = painterResource(id = item.first),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(top = 48.dp),
                        text = item.second, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)
                    )

                    Text(
                        modifier = Modifier.padding(top = 8.dp, bottom = 40.dp),
                        text = item.third, style = getTextStyle(textStyle = RememberTextStyle.BODY_2)
                    )
                }
            }
        }

//        Spacer(modifier = Modifier.weight(1f))

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth().padding(top = 16.dp),
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
            RememberFilledButton(text = "시작하기", onClick = {
                navController.navigate(RememberScreen.Intro.name) {
                    popUpTo(RememberScreen.OnBoarding.name) {
                        inclusive = true
                    }
                }
            })
        } else {
            RememberOutlinedButton(text = "건너뛰기", onClick = {
                navController.navigate(RememberScreen.Intro.name) {
                    popUpTo(RememberScreen.OnBoarding.name) {
                        inclusive = true
                    }
                }
            })

        }
    }
}

@Preview
@Composable
fun PreviewOnBoardingScreen() {
    OnBoardingScreen(navController = rememberNavController())
}