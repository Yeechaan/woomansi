package com.lee.remember.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navHostController: NavHostController) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(lightColor)) {
        TopAppBar(
            title = {},
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(modifier = Modifier.width(62.dp), onClick = {}) {
                    Icon(
                        painterResource(id = R.drawable.logo_title),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
            actions = {
                IconButton(onClick = {
//                    navHostController.navigate(RememberScreen.FriendEdit.name)
                }) {
                    Icon(painter = painterResource(id = R.drawable.ic_account), contentDescription = "")
                }
            }
        )

        Row(
            modifier = Modifier
                .padding(top = 24.dp, start = 48.dp, end = 48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HistoryItem("전화", R.drawable.ic_call) {}
            HistoryItem("SNS", R.drawable.ic_message) {}
            HistoryItem("안부", R.drawable.ic_sns) {}
        }

        if (friendProfiles.isEmpty()) {
            HistoryEmptyScreen()
        } else {
            HistoryPagerScreen(navHostController)
        }
    }
}

@Composable
fun HistoryEmptyScreen() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 90.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "등록된 친구가 없네요.\n새로운 친구를 등록해보세요!",
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0x94000000), textAlign = TextAlign.Center)
        )

        Image(painterResource(id = R.drawable.ic_empty_friend), contentDescription = null, modifier = Modifier.padding(top = 22.dp))

        Button(
            onClick = {},
            modifier = Modifier.padding(top = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            androidx.compose.material3.Text(
                text = "친구 추가",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryPagerScreen(navHostController: NavHostController) {

    val pagerState = rememberPagerState(pageCount = { friendProfiles.size })
    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                        (pagerState.currentPage - page) + pagerState
                            .currentPageOffsetFraction
                        ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.img_sample),
                    contentDescription = stringResource(id = R.string.history),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.fillMaxSize(),
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    val friendProfile = friendProfiles[page]
                    FriendSummaryItem(friendProfile, navHostController)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(text: String, icon: Int, onClick: () -> Unit) {
    Column(modifier = Modifier.width(64.dp)) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp),  //avoid the oval shape
            shape = CircleShape,
            border = BorderStroke(1.dp, Color(0xFFD8D8D8)),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
        ) {
            Icon(painterResource(id = icon), contentDescription = "content description", modifier = Modifier.padding(8.dp))
        }
        Text(
            text = text, textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun FriendSummaryItem(friendProfile: FriendProfile, navHostController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = friendProfile.name, fontSize = 24.sp, color = Color.White)
            Text(text = friendProfile.phoneNumber, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp), color = Color.White)
            Text(
//                text = stringResource(id = R.string.birth_date, birthMonth, birthDate),
                text = friendProfile.birthDate,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Button(
            onClick = {
                selectedFriendPhoneNumber = friendProfile.phoneNumber
                navHostController.navigate(RememberScreen.FriendHistory.name)
            },
            modifier = Modifier.size(70.dp),  //avoid the oval shape
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
        ) {
            Text(text = "기록\n보기")
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(rememberNavController())

//    HistoryEmptyScreen()
}