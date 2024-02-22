package com.lee.remember.android.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingScreen(navController: NavHostController){
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("안부 묻기", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            },
            actions = {
//                Text(
//                    "수정하기",
//                    Modifier
//                        .padding(end = 12.dp)
//                        .clickable {
//                            navHostController.navigate("${RememberScreen.FriendEdit.name}/${friendId}")
//                        },
//                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF33322E)),
//                )
            },
            modifier = Modifier
                .shadow(elevation = 1.dp, spotColor = Color(0x36444444), ambientColor = Color(0x36444444))
                .shadow(elevation = 10.dp, spotColor = Color(0x0F444444), ambientColor = Color(0x0F444444))
        )
    }

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_meeting_empty), contentDescription = null)
        Text(
            "더 나은 서비스를 위해 준비중이에요.\n조금만 기다려주세요!",
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0x94000000)), textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 28.dp),
        )
    }
}