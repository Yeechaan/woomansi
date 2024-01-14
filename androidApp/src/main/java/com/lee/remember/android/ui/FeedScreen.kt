package com.lee.remember.android.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.RememberTopAppBar
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.rememberFontFamily
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.parseUtcString
import com.lee.remember.repository.MemoryRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navHostController: NavHostController) {
    val items = remember { mutableStateOf<List<FriendHistory>>(listOf()) }

    LaunchedEffect(Unit) {
        val memoryResponse = MemoryRepository().getMemoryList()
        val memories = memoryResponse.getOrNull()?.result?.map {
            val friends = it.friends.map { it.name }

            FriendHistory(
                title = it.title ?: "",
                contents = it.description ?: "",
                image = it.thumbnail?.image ?: "",
                date = parseUtcString(it.date ?: ""),
                ownerFriendName = friends.firstOrNull() ?: "",
                friendNames = if (friends.isNotEmpty()) friends.subList(1, friends.size) else listOf()
            )
        } ?: listOf()

        items.value = memories
    }

    Column(
        Modifier.background(lightColor)
    ) {
        RememberTopAppBar(navHostController)

//        var selectedIndex by remember { mutableStateOf(0) }
//        val options = listOf("이름 순", "최신 순")
//
//        SingleChoiceSegmentedButtonRow(
//            Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            options.forEachIndexed { index, label ->
//                SegmentedButton(
//                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
//                    onClick = { selectedIndex = index },
//                    selected = index == selectedIndex,
//                    colors = SegmentedButtonDefaults.colors(activeContainerColor = fontColorPoint, inactiveContainerColor = whiteColor)
//                ) {
//                    Text(label, style = getTextStyle(textStyle = RememberTextStyle.BODY_2B))
//                }
//            }
//        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightColor)
        ) {
            LazyColumn(
                Modifier.padding(start = 16.dp, end = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.padding(top = 10.dp)) }

                items(items.value) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .border(width = 1.dp, color = Color(0xFFD8D8D8), shape = RoundedCornerShape(size = 16.dp))
                    ) {
                        FeedItem(item)
                    }
                }
            }
        }
    }

}

@Composable
fun FeedItem(friendHistory: FriendHistory, isFriendInfoVisible: Boolean = true) {
    Column(
        Modifier.background(Color.White)
    ) {
        if (isFriendInfoVisible) {
            Row(
                Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xffEFEEEC)),
                    contentAlignment = Alignment.Center
                ) {
                    if (friendHistory.imageUri != null) {
//                        val bitmap: Bitmap? = stringToBitmap(image)
//                        bitmap?.let {
//                            Image(
//                                bitmap = bitmap.asImageBitmap(), contentDescription = null,
//                                contentScale = ContentScale.Crop,
//                                modifier = Modifier.fillMaxSize()
//                            )
//                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera_32),
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(Color(0xff1D1B20)),
                            modifier = Modifier.padding(6.dp),
                        )
                    }
                }

                Column(
                    Modifier
                        .padding(start = 10.dp)
                        .weight(1f)
                ) {
                    Text(text = "with", fontSize = 10.sp, color = Color(0xFF1D1B20), fontFamily = rememberFontFamily)
                    Text(
                        text = friendHistory.ownerFriendName,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
                        color = fontColorBlack
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }

        if (friendHistory.image.isNotEmpty()) {
            val bitmap: Bitmap? = stringToBitmap(friendHistory.image)
            bitmap?.let {
                Image(
                    bitmap = bitmap.asImageBitmap(), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(218.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Row(
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = friendHistory.title, style = getTextStyle(textStyle = RememberTextStyle.BODY_2B), color = fontColorBlack)
                Text(
                    text = "2023. 10. 23",
                    fontSize = 10.sp,
                    color = fontColorBlack,
                    fontFamily = rememberFontFamily,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }


            var expanded by remember { mutableStateOf(false) }
            Icon(
                modifier = Modifier.clickable { expanded = true },
                painter = painterResource(id = R.drawable.ic_more), contentDescription = "more",
                tint = Color.Black
            )

            Row(horizontalArrangement = Arrangement.End) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White)
//                        .padding(start = 16.dp, end = 16.dp, top = 28.dp)
                ) {
                    DropdownMenuItem(text = {
                        Text(
                            text = "수정",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_3B).copy(fontColorBlack)
                        )
                    },
                        onClick = {
                            // Todo navigate to FriendEditScreen
                            expanded = false
                        })

                    DropdownMenuItem(text = {
                        Text(
                            text = "삭제",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_3B).copy(Color(0xFFB3661E))
                        )
                    },
                        onClick = {
                            // Todo delete feed item from server, local
                            expanded = false
                        })
                }
            }
        }

        Text(
            text = friendHistory.contents,
            style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFF49454F)),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxWidth()
        )

        val friendNames = if (friendHistory.friendNames.isNotEmpty()) friendHistory.friendNames.joinToString(prefix = "#") else ""
        Text(
            text = friendNames,
            style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFF49454F)),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp)
                .fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun PreviewFeedScreen() {
//    FeedScreen(rememberNavController())

    FeedItem(FriendHistory(title = "libris", contents = "curae", imageUri = null), true)
}