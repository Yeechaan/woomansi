package com.lee.remember.android.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.RememberTopAppBar
import com.lee.remember.android.accessToken
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.remote.FriendApi
import com.lee.remember.request.FriendListResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

val whiteColor = Color(0xFFFFFFFF)
val lightColor = Color(0xFFF7F7F7)
val pointColor = Color(0xFFF3F2EE)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(navHostController: NavHostController) {

    val friendList = remember { mutableStateOf(mutableListOf<FriendListResponse.FriendSummaryInfo>()) }
    val scope = rememberCoroutineScope()
    scope.launch {
        try {
            val response = FriendApi().getFriendList(accessToken)
            if (response != null) {
                friendList.value.addAll(response.result)
            }
        } catch (e: Exception) {
            e.localizedMessage ?: "error"
        }

        scope.cancel()
    }

    Column {
        RememberTopAppBar()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightColor)
        ) {
            LazyColumn(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                val grouped = mapOf("목록" to friendList.value)

                grouped.forEach { (initial, contactsForInitial) ->
                    stickyHeader {
                        CharacterHeader(initial)
                    }

                    items(contactsForInitial) { contact ->
                        val friendProfile = FriendProfile(name = contact.name, phoneNumber = contact.phoneNumber ?: "")
                        FriendItem(friendProfile, navHostController)
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navHostController.navigate(RememberScreen.FriendEdit.name)
                },
                containerColor = Color(0xFFF2C678),
                contentColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_faq),
                    contentDescription = "camera_image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun CharacterHeader(string: String) {
    Text(
        text = string,
        style = getTextStyle(textStyle = RememberTextStyle.HEAD_5),
        modifier = Modifier
            .background(color = lightColor)
            .fillMaxWidth()
            .padding(top = 4.dp)
    )
}

@Composable
fun FriendItem(friendProfile: FriendProfile, navHostController: NavHostController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp)
                .clickable {
                    selectedFriendPhoneNumber = friendProfile.phoneNumber
                    navHostController.navigate(RememberScreen.FriendProfile.name)
                },
            colors = CardDefaults.cardColors(Color.White),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = "camera_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(pointColor)
                )

                Column(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = friendProfile.name,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_1B)
                    )
                    Text(
                        text = friendProfile.phoneNumber,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_3)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.baseline_menu_24),
                    contentDescription = "menu_image"
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFriendScreen() {
    FriendScreen(navHostController = rememberNavController())
}