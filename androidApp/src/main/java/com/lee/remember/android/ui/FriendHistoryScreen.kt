package com.lee.remember.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendHistoryScreen(navHostController: NavHostController) {

    val friendProfile = friendProfiles.find { it.phoneNumber == selectedFriendPhoneNumber }
    val items = friendProfile?.history ?: listOf<FriendHistory>()

    Column {
        TopAppBar(
            title = { Text(friendProfile?.name ?: "", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
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
                        FeedItem(friendProfile?.name ?: "", item)
                    }
                }
            }

            FloatingActionButton(
                onClick = {

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

@Preview
@Composable
fun PreviewFriendHistoryScreen() {
    FriendHistoryScreen(rememberNavController())
}