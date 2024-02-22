package com.lee.remember.android.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.ui.friend.ContactType
import com.lee.remember.android.ui.friend.FriendContactDialog
import com.lee.remember.android.ui.friend.lightColor
import com.lee.remember.android.ui.friend.stringToBitmap
import com.lee.remember.android.ui.friend.whiteColor
import com.lee.remember.android.ui.memory.fontPointColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.HistoryViewModel
import com.lee.remember.local.model.FriendRealm
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navHostController: NavHostController,
    viewModel: HistoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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
        Column(Modifier.verticalScroll(scrollState)) {
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

            if (uiState.friends.isEmpty()) {
                HistoryEmptyScreen(navHostController)
            } else {
                HistoryPagerScreen(navHostController, uiState.friends) {
                    currentFriendIndex.value = it
                }
            }
        }

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        if (showBottomSheet && currentFriendIndex.value != -1) {
            val friend = uiState.friends[currentFriendIndex.value]
            val name = friend.name

            val contactType = if (isCall) ContactType.Call else ContactType.Message
            FriendContactDialog(
                name = name,
                contactType = contactType,
                onDismissRequest = { showBottomSheet = false },
                onConfirm = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            val callIntent: Intent = if (isCall) {
                                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${friend.phoneNumber}"))
                            } else {
                                Intent(Intent.ACTION_SENDTO, Uri.parse("sms:${friend.phoneNumber}"))
                            }

                            startActivity(context, callIntent, null)

                            showBottomSheet = false
                        }
                    }
                }
            )
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
                navHostController.navigate("${RememberScreen.FriendAdd.name}/${null}/${null}")
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
fun HistoryPagerScreen(navHostController: NavHostController, friendList: List<FriendRealm>, onCurrentPage: (Int) -> Unit) {

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
        OutlinedCard(
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
                },
            border = BorderStroke(1.dp, color = Color(0xFFD8D8D8)),
            elevation = CardDefaults.cardElevation(1.dp)
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
                    val friendId = friendProfile.id
                    navHostController.navigate("${RememberScreen.MemoryFriend.name}/${friendId}")
                }
                .fillMaxSize()
                .background(whiteColor)
            ) {
                Column(
//                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top
                ) {
                    FriendSummaryItem(friendProfile)
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
                    val imageIndex = (page % 5)
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
fun FriendSummaryItem(friendProfile: FriendProfile) {
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
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(rememberNavController())
}