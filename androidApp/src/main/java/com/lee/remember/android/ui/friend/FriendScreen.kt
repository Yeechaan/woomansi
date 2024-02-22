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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.ui.intro.getContracts
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.FriendViewModel
import org.koin.androidx.compose.koinViewModel

val whiteColor = Color(0xFFFFFFFF)
val lightColor = Color(0xFFF7F7F7)
val pointColor = Color(0xFFF3F2EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(
    navHostController: NavHostController,
    viewModel: FriendViewModel = koinViewModel(),
) {
    val friendList = remember {
        mutableStateOf(viewModel.getFriends())
    }

    Column(
        Modifier
            .background(lightColor)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
//                .padding(top = 16.dp)
                .background(lightColor)
        ) {
            if (friendList.value.isEmpty()) {
                EmptyFriendScreen()
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 80.dp, bottom = 100.dp),
                    painter = painterResource(id = R.drawable.ic_empty_button_guide), contentDescription = null
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    item { Spacer(modifier = Modifier.padding(top = 10.dp)) }

                    items(friendList.value) { contact ->
                        val friendProfile = FriendProfile(
                            id = contact.id,
                            name = contact.name,
                            phoneNumber = contact.phoneNumber ?: "",
                            image = contact.profileImage?.image ?: ""
                        )
                        FriendItem(friendProfile, navHostController)
                    }

                    item { Spacer(modifier = Modifier.padding(bottom = 44.dp)) }
                }
            }

            var showBottomSheet by remember { mutableStateOf(false) }
            val sheetState = androidx.compose.material3.rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()

            val openAlertDialog = remember { mutableStateOf(false) }
            if (openAlertDialog.value) {
                FriendAddDialog(
                    onDismissRequest = {
                        openAlertDialog.value = false
                    }, onNewClicked = {
                        openAlertDialog.value = false

                        // 새로운 친구 추가
                        navHostController.navigate("${RememberScreen.FriendAdd.name}/${null}/${null}")
                    },
                    onContactsClicked = {
                        openAlertDialog.value = false
                        showBottomSheet = true
                    }
                )
            }

            // 전화번호 목록
            val contracts = mutableListOf<Contract>()
            contracts.addAll(getContracts())

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = Color.White,
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                ) {
                    LazyColumn(
                        Modifier.padding(start = 16.dp, end = 16.dp)
                    ) {
                        items(contracts) { contract ->
                            Row(
                                modifier = Modifier.clickable {
                                    showBottomSheet = false
                                    // 연락처 친구 추가
                                    navHostController.navigate("${RememberScreen.FriendAdd.name}/${contract.name}/${contract.number}")
                                },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 18.dp),
                                    text = contract.name,
                                    style = getTextStyle(textStyle = RememberTextStyle.BODY_1B)
                                )
                                Text(
                                    text = contract.number,
                                    style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0x61000000))
                                )
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    openAlertDialog.value = true
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
                .padding(top = 4.dp, bottom = 4.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        navHostController.navigate("${RememberScreen.FriendProfile.name}/${friendProfile.id}")
                    }
            ) {
                Spacer(modifier = Modifier.padding(start = 16.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xffEFEEEC)),
                    contentAlignment = Alignment.Center
                ) {
                    if (friendProfile.image.isNotEmpty()) {
                        val bitmap: Bitmap? = stringToBitmap(friendProfile.image)
                        bitmap?.let {
                            Image(
                                bitmap = bitmap.asImageBitmap(), contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera_32),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color(0xff1D1B20)),
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .padding(start = 16.dp)
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
            }
        }
    }
}

@Composable
fun EmptyFriendScreen() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_friends), contentDescription = null)
        Text(
            "등록된 친구가 없네요.\n새로운 친구를 등록해보세요!",
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0x94000000)), textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 28.dp),
        )
    }
}

@Preview
@Composable
fun PreviewFriendScreen() {
    FriendScreen(navHostController = rememberNavController())
}