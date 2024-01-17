package com.lee.remember.android.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.RememberTopAppBar
import com.lee.remember.android.accessToken
import com.lee.remember.android.bottomPadding
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.request.FriendResponse
import io.github.aakira.napier.Napier
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navHostController: NavHostController) {

    val friendList = remember { mutableStateOf(mutableListOf<FriendResponse.Result>()) }
    val currentFriendIndex = remember { mutableStateOf<Int>(-1) }

    var showBottomSheet by remember { mutableStateOf(false) }
    var isCall by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .background(lightColor)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RememberTopAppBar(navHostController)

        Row(
            modifier = Modifier
                .padding(top = 24.dp, start = 48.dp, end = 48.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            HistoryItem("전화", R.drawable.ic_call) {
                showBottomSheet = true
                isCall = true
            }
            HistoryItem("SMS", R.drawable.ic_message) {
                showBottomSheet = true
                isCall = false
            }
            HistoryItem("안부", R.drawable.ic_sns) {
                navHostController.navigate(RememberScreen.Meeting.name)
            }
        }

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        val imageId = if (isCall) R.drawable.ic_call else R.drawable.ic_message
        val title = if (isCall) "안심하세요!\n바로 통화로 연결되지 않아요." else "친구에게 문자를 보내보세요."
        val buttonText = if (isCall) "전화하기" else "문자하기"

        if (showBottomSheet && currentFriendIndex.value != -1) {
            val phoneNumber = friendList.value[currentFriendIndex.value].phoneNumber ?: ""

            ModalBottomSheet(
                containerColor = Color.White,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(modifier = Modifier.padding(top = 36.dp), painter = painterResource(id = imageId), contentDescription = null)

                    Text(
                        modifier = Modifier.padding(top = 24.dp),
                        text = title,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.Black),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier.padding(top = 40.dp),
                        text = phoneNumber,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.Black)
                    )

                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, bottom = bottomPadding)
                            .background(Color(0xFFF2BE2F)),
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    val callIntent: Intent = if (isCall) {
                                        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                                    } else {
                                        Intent(Intent.ACTION_SENDTO, Uri.parse("sms:$phoneNumber"))
                                    }

                                    startActivity(context, callIntent, null)

                                    showBottomSheet = false
                                }
                            }
                        }
                    ) {
                        Text(buttonText, style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF50432E)))
                    }
                }
            }
        }

        val apiScope = rememberCoroutineScope()
        apiScope.launch {
            try {
                val response = FriendApi().getFriendList(accessToken)
                if (response?.result != null) {
                    friendList.value = response.result?.toMutableList() ?: mutableListOf()

                    response.toString()
                } else {
                    Napier.d("### ${response?.resultCode}")
                }
            } catch (e: Exception) {
                e.localizedMessage ?: "error"
            }

            apiScope.cancel()
        }

        Column(Modifier.verticalScroll(scrollState)) {
            if (friendList.value.isEmpty()) {
                HistoryEmptyScreen(navHostController)
            } else {
                HistoryPagerScreen(navHostController, friendList.value) {
                    currentFriendIndex.value = it
                }
            }
        }

    }
}

@Composable
fun HistoryEmptyScreen(navHostController: NavHostController) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 90.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "등록된 친구가 없네요.\n새로운 친구를 등록해보세요!",
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0x94000000), textAlign = TextAlign.Center)
        )

        Image(painterResource(id = R.drawable.ic_friends), contentDescription = null, modifier = Modifier.padding(top = 16.dp))

        Button(
            onClick = {
                val friendId = "-1"
                navHostController.navigate("${RememberScreen.FriendEdit.name}/${friendId}")
            },
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            androidx.compose.material3.Text(
                text = "친구 추가",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 32.dp)
            )
        }

        Spacer(modifier = Modifier.fillMaxSize())
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryPagerScreen(navHostController: NavHostController, friendList: MutableList<FriendResponse.Result>, onCurrentPage: (Int) -> Unit) {

    val pagerState = rememberPagerState(pageCount = { friendList.size })
    onCurrentPage(pagerState.currentPage)

    val emptyFriendImages = listOf(
        R.drawable.img_friends_default_1,
        R.drawable.img_friends_default_2,
        R.drawable.img_friends_default_3,
        R.drawable.img_friends_default_4,
        R.drawable.img_friends_default_5
    )

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
    ) { page ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .height(460.dp)
                .padding(horizontal = 4.dp)
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            val friend = friendList[page]
            val friendProfile = FriendProfile(
                id = friend.id,
                name = friend.name,
                phoneNumber = friend.phoneNumber ?: "",
                image = friend.profileImage?.image ?: ""
            )

            val brush = Brush.verticalGradient(listOf(Color(0x77000000), Color.White))

            Column(modifier = Modifier
                .clickable {
                    selectedFriendPhoneNumber = friendProfile.phoneNumber

                    val friendId = friendProfile.id
                    navHostController.navigate("${RememberScreen.FriendHistory.name}/${friendId}")
                }
                .fillMaxSize()
                .background(whiteColor)
            ) {
                Column(
//                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    FriendSummaryItem(friendProfile, navHostController)
                }

                val bitmap: Bitmap? = stringToBitmap(friendProfile.image)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(), contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .height(312.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imageIndex  = (page % 5)
                    val image = emptyFriendImages[imageIndex]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(312.dp), contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = stringResource(id = R.string.history),
//                            modifier = Modifier.height(460.dp)
                        )
                    }
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
            modifier = Modifier.size(64.dp),  //avoid the oval shape
            shape = CircleShape,
            border = BorderStroke(1.dp, Color(0xFFD8D8D8)),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black, containerColor = Color.White),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
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
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = friendProfile.name, style = getTextStyle(textStyle = RememberTextStyle.HEAD_3), color = Color(0xFF000000))
            Text(
                text = friendProfile.phoneNumber,
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B),
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF000000)
            )
            Text(
//                text = stringResource(id = R.string.birth_date, birthMonth, birthDate),
                text = friendProfile.birthDate,
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1),
                color = Color.White
            )
        }
//        Button(
//            onClick = {
//                selectedFriendPhoneNumber = friendProfile.phoneNumber
//
//                val friendId = friendProfile.id
//                navHostController.navigate("${RememberScreen.FriendHistory.name}/${friendId}")
//            },
//            modifier = Modifier.size(70.dp),  //avoid the oval shape
//            shape = CircleShape,
//            contentPadding = PaddingValues(0.dp),  //avoid the little icon
//            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
//        ) {
//            Text(text = "기록\n보기", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))
//        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionConfirmModel(phoneNumber: String, isCall: Boolean) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imageId = if (isCall) R.drawable.ic_call else R.drawable.ic_message
    val title = if (isCall) "안심하세요!\n바로 통화로 연결되지 않아요." else "친구에게 문자를 보내보세요."
    val buttonText = if (isCall) "전화하기" else "문자하기"

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
        ) {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(modifier = Modifier.padding(top = 36.dp), painter = painterResource(id = imageId), contentDescription = null)

                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = title,
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.Black)
                )

                Text(
                    modifier = Modifier.padding(top = 40.dp),
                    text = phoneNumber,
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.Black)
                )

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 40.dp)
                        .background(Color(0xFFF8D393)),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                val callIntent: Intent = Uri.parse("tel:$phoneNumber").let { number ->
                                    Intent(Intent.ACTION_DIAL, number)
                                }
                                startActivity(context, callIntent, null)

                                showBottomSheet = false
                            }
                        }
                    }
                ) {
                    Text(buttonText, style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF50432E)))
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(rememberNavController())

//    HistoryEmptyScreen()
}