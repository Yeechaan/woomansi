package com.lee.remember.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.parseUtcString
import com.lee.remember.android.viewmodel.FeedViewModel
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.remote.FriendApi
import com.lee.remember.repository.MemoryRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendHistoryScreen(
    navHostController: NavHostController, friendId: String?,
    viewModel: FeedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    viewModel.initFeedState()

    var name by remember { mutableStateOf("") }

//    LaunchedEffect(null) {
//        try {
//            val response = FriendApi().getFriend(accessToken, friendId ?: "")
//
//            if (response != null) {
//                response.result?.let {
//                    name = it.name
//                }
//
//                response.toString()
//            }
//        } catch (e: Exception) {
//            Napier.d("### ${e.localizedMessage}")
//            e.localizedMessage ?: "error"
//        }
//    }

    val items = remember { mutableStateOf<List<FriendHistory>>(listOf()) }
    LaunchedEffect(viewModel.uiState) {
        viewModel.uiState.collectLatest { uiState ->
            name = uiState.name
            items.value = uiState.memories.sortedByDescending { it.date }
        }
    }

//    LaunchedEffect(Unit) {
//        val memoryResponse = MemoryRepository().getMemoryList()
//        val memories = memoryResponse.getOrNull()?.result?.filter { it.friends.firstOrNull()?.id.toString() == friendId }?.map {
//            val friends = it.friends.map { it.name }
//
//            FriendHistory(
//                title = it.title ?: "",
//                contents = it.description ?: "",
//                image = it.thumbnail?.image ?: "",
//                date = parseUtcString(it.date ?: ""),
//                ownerFriendName = friends.firstOrNull() ?: "",
//                friendNames = if (friends.isNotEmpty()) friends.subList(1, friends.size) else listOf()
//            )
//        } ?: listOf()
//
//        items.value = memories
//    }

    Column {
        TopAppBar(
            title = { Text(name, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color.White
            ),
            navigationIcon = {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightColor)
        ) {
            if (items.value.isEmpty()) {
                EmptyFriendHistoryScreen()
                Image(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 80.dp, bottom = 100.dp),
                    painter = painterResource(id = R.drawable.ic_empty_button_guide), contentDescription = null
                )
            } else {
                LazyColumn(
                    Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    item { Spacer(modifier = Modifier.padding(top = 10.dp)) }
                    items(items.value) { item ->
                        OutlinedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            border = BorderStroke(1.dp, color = Color(0xFFD8D8D8)),
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                        ) {
                            FeedItem(
                                friendHistory = item,
                                isFriendInfoVisible = false,
                                onUpdate = {
//                                viewModel.
                                },
                                onDelete = {
                                    viewModel.deleteMemory(item.id)
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navHostController.navigate("${RememberScreen.HistoryAdd.name}/${friendId}")
                },
                containerColor = Color(0xFFF2C678),
                contentColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_write),
                    contentDescription = "camera_image",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun EmptyFriendHistoryScreen() {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.img_empty_history), contentDescription = null)
        Text(
            "친구와의 추억을 기록해보세요!",
            style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0x94000000)),
            modifier = Modifier.padding(top = 28.dp),
        )
    }
}

@Preview
@Composable
fun PreviewFriendHistoryScreen() {
    FriendHistoryScreen(rememberNavController(), null)
}