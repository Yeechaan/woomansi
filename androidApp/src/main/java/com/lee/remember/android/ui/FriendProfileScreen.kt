package com.lee.remember.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

// 0xFF1D1B20
val fontColorBlack = Color(0xFF1D1B20)
val fontColorPoint = Color(0xFFF2BE2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(navHostController: NavHostController) {

    val friendProfile = friendProfiles.find { it.phoneNumber == selectedFriendPhoneNumber }

    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(lightColor)
            .verticalScroll(scrollState)
    ) {
        TopAppBar(
            title = { Text("친구 기록", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            },
            actions = {
                TextButton(onClick = {
                    navHostController.navigate(RememberScreen.FriendEdit.name)
                }) {
                    Text(text = "편집", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = Color(0xFF49454F)))
                }
            },
            modifier = Modifier
                .shadow(elevation = 1.dp, spotColor = Color(0x36444444), ambientColor = Color(0x36444444))
                .shadow(elevation = 10.dp, spotColor = Color(0x0F444444), ambientColor = Color(0x0F444444))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(lightColor)
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera), contentDescription = "",
                modifier = Modifier.size(74.dp)
            )
        }

        Divider(thickness = 1.dp, color = Color.Black)

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            var name by remember { mutableStateOf(friendProfile?.name ?: "") }
            var group by remember { mutableStateOf(friendProfile?.grouped ?: "") }
            var number by remember { mutableStateOf(friendProfile?.phoneNumber ?: "") }
            var dateTitle by remember { mutableStateOf("기념일") }
            var date by remember { mutableStateOf("2023/11/10") }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_5).copy(fontColorBlack),
                )

                Button(
                    onClick = { navHostController.navigate(RememberScreen.FriendEdit.name) },
                    modifier = Modifier,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(size = 8.dp),
                    border = BorderStroke(1.dp, Color(0xFF79747E))
                ) {
                    Text(
                        text = "수정하기",
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF49454F)),
                        modifier = Modifier
                    )
                }
            }


            TextField(
                value = group, onValueChange = { group = it }, readOnly = true,
                label = {
                    Text(
                        "그룹",
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_4)
                    )
                },
                textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = number, onValueChange = { number = it }, readOnly = true,
                label = {
                    Text("연락처", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                },
                textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 12.dp)) {
                TextButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .padding()
                ) {
                    Text(
                        dateTitle,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = Color(0xFF1D1B20))
                    )
//                    Icon(painter = painterResource(id = R.drawable.baseline_expand_more_24), contentDescription = "", tint = Color.Black)
                }

                TextField(
                    value = date, onValueChange = { date = it }, readOnly = true,
                    label = {
                        Text("이벤트", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                    },
                    textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calander),
                                contentDescription = "Clear"
                            )
                        }
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun PreviewFriendProfileScreen() {
    FriendProfileScreen(rememberNavController())
}