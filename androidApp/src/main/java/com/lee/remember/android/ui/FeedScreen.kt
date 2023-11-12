package com.lee.remember.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonBorder
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lee.remember.android.R
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(navHostController: NavHostController) {

    val items = mutableListOf<FriendHistory>()
    friendProfiles.map {
        items.addAll(it.history)
    }

//    val items = listOf("1", "2", "1", "2", "1", "2")

    Column(
        Modifier.background(lightColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    "우리가 만났던 시절",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_4),
                )
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            navigationIcon = {
                IconButton(onClick = {
//                    navHostController.navigateUp()
                }) {
                    Icon(
                        painterResource(id = R.drawable.logo_app),
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

        Row(Modifier.fillMaxWidth().padding(16.dp)) {
            Button(onClick = { /*TODO*/ }, Modifier.weight(1f).padding(end = 8.dp)) {
                Text(text = "이름 순")
            }
            Button(onClick = { /*TODO*/ }, Modifier.weight(1f).padding(start = 8.dp)) {
                Text(text = "최신 순")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightColor)
        ) {
            LazyColumn(
                Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                items(items) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        FeedItem(item)
                    }
                }
            }
        }
    }

}

@Composable
fun FeedItem(friendHistory: FriendHistory) {
    Column(
        Modifier.background(Color.White)
    ) {
        Row(
            Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_camera),
//                contentDescription = "camera_image",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(36.dp)
//                    .clip(CircleShape)
////                    .background(pointColor)
//            )
            AsyncImage(
                model = friendHistory.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(218.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                Modifier
                    .padding(start = 10.dp)
                    .weight(1f)
            ) {
                Text(text = "with", fontSize = 10.sp, color = Color.Black)
                Text(text = "harry", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B), color = Color.Black)
            }
            Icon(painter = painterResource(id = R.drawable.ic_more), contentDescription = "more")
        }

//        Image(
//            painter = painterResource(id = R.drawable.img_feed), contentDescription = "",
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(218.dp)
//                .padding(top = 10.dp)
//                .background(lightColor)
//        )

        Row(
            Modifier
                .padding(start = 16.dp, end = 16.dp, top = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = friendHistory.title, style = getTextStyle(textStyle = RememberTextStyle.BODY_2B), color = Color.Black)
            Text(text = "2023. 10. 23", fontSize = 10.sp, color = Color.Black)
        }

        Text(
            text = friendHistory.contents,
            style = getTextStyle(textStyle = RememberTextStyle.BODY_4),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 28.dp)
                .fillMaxWidth(), color = Color.Black
        )
    }
}


@Preview
@Composable
fun PreviewFeedScreen() {
    FeedScreen(rememberNavController())
}