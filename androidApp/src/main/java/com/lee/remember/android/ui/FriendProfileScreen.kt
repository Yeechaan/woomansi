package com.lee.remember.android.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import coil.compose.AsyncImage
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.parseUtcString
import com.lee.remember.remote.FriendApi
import io.github.aakira.napier.Napier
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// 0xFF1D1B20
val fontColorBlack = Color(0xFF1D1B20)
val fontColorPoint = Color(0xFFF2BE2F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendProfileScreen(navHostController: NavHostController, friendId: String?) {

    var image by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var dateTitle by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    scope.launch {
        try {
            val response = FriendApi().getFriend(accessToken, friendId ?: "")

            if (response != null) {
//                Napier.d("###hi ${response}")

                response.result?.let {
                    name = it.name
                    group = "-"  // Todo need response field
                    number = it.phoneNumber ?: ""
                    dateTitle = it.events?.firstOrNull()?.name ?: "기념일"
                    date = parseUtcString(it.events?.firstOrNull()?.date ?: "")
                    image = it.profileImage?.image ?: ""
                }

                response.toString()
            }
        } catch (e: Exception) {
            Napier.d("### ${e.localizedMessage}")
            e.localizedMessage ?: "error"
        }

        scope.cancel()
    }

    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .background(whiteColor)
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
            modifier = Modifier
                .shadow(elevation = 1.dp, spotColor = Color(0x36444444), ambientColor = Color(0x36444444))
                .shadow(elevation = 10.dp, spotColor = Color(0x0F444444), ambientColor = Color(0x0F444444))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(lightColor),
            contentAlignment = Alignment.Center
        ) {
            if (image.isNotEmpty()) {
                val bitmap: Bitmap? = stringToBitmap(image)
                bitmap?.let {
                    Image(
                        bitmap = bitmap.asImageBitmap(), contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_camera), contentDescription = "",
                    modifier = Modifier.size(48.dp)
                )
            }

        }

        Divider(thickness = 1.dp, color = Color.Black)

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_5).copy(fontColorBlack),
                )

                Button(
                    onClick = { navHostController.navigate("${RememberScreen.FriendEdit.name}/${friendId}") },
                    modifier = Modifier.defaultMinSize(minHeight = 22.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(size = 8.dp),
                    border = BorderStroke(1.dp, Color(0xFF79747E)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "수정하기",
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF49454F)),
                        modifier = Modifier
                    )
                }
            }

            FriendProfileItem("그룹", group)
            FriendProfileItem("연락처", number)
            FriendProfileItem(dateTitle, date)

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(size = 100.dp),
                border = BorderStroke(1.dp, fontColorPoint)
            ) {
                Text(
                    text = "친구 기록보기",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(fontColorPoint),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
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