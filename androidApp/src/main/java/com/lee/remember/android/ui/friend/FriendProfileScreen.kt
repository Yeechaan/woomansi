package com.lee.remember.android.ui.friend

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberOutlinedButton
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.FriendProfileViewModel
import com.lee.remember.local.model.FriendRealm
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

// 0xFF1D1B20
val fontColorBlack = Color(0xFF1D1B20)
val fontColorPoint = Color(0xFFFFCF40)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(
    navHostController: NavHostController,
    friendId: String?,
    viewModel: FriendProfileViewModel = koinViewModel(parameters = { parametersOf(friendId?.toInt()) }),
) {
    val uiState by viewModel.uiState.collectAsState()

    val friend = uiState.friend ?: FriendRealm()
    val image = friend.profileImage?.image ?: ""

    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(friend.name, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            },
            actions = {
                Text(
                    "수정하기",
                    Modifier
                        .padding(end = 12.dp)
                        .clickable {
                            navHostController.navigate("${RememberScreen.FriendEdit.name}/${friendId}")
                        },
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF33322E)),
                )
            },
            modifier = Modifier
                .shadow(elevation = 1.dp, spotColor = Color(0x36444444), ambientColor = Color(0x36444444))
                .shadow(elevation = 10.dp, spotColor = Color(0x0F444444), ambientColor = Color(0x0F444444))
        )

        if (image.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xffEFEEEC)),
                contentAlignment = Alignment.Center
            ) {
                val bitmap: Bitmap? = stringToBitmap(image)
                bitmap?.let {
                    Image(
                        bitmap = bitmap.asImageBitmap(), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.padding(top = 32.dp))
        }

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            FriendProfileItem("연락처", friend.phoneNumber)
//            FriendProfileItem("그룹", group)
            FriendProfileItem(friend.events.firstOrNull()?.name ?: "기념일", friend.events.firstOrNull()?.date ?: "-")
        }

        RememberOutlinedButton(text = "친구 기록 보기", onClick = {
            navHostController.navigate("${RememberScreen.MemoryFriend.name}/${friendId}")
        })
    }
}

@Composable
fun FriendProfileItem(title: String, contents: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(color = Color(0xFF49454F)))
            Text(contents, style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(color = Color(0xFF1D1B20)))
        }

        Divider(thickness = 1.dp, color = Color(0xFFD1D3D8))
    }
}

@Preview
@Composable
fun PreviewFriendProfileScreen() {
    FriendProfileScreen(rememberNavController(), null)
}