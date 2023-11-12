package com.lee.remember.android.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coil.compose.AsyncImage
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

//0xFFD59519
val fontPointColor = Color(0xFFD59519)
val fontHintColor = Color(0x4D000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryAddScreen(navHostController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        Modifier.verticalScroll(scrollState)
    ) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        var selectedImages by remember {
            mutableStateOf<List<Uri?>>(emptyList())
        }

        TopAppBar(
            title = { Text("기록 추가", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
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
            actions = {
                TextButton(onClick = {
                    friendProfiles.find { it.phoneNumber == selectedFriendPhoneNumber }?.history?.add(
                        FriendHistory(
                            title,
                            content,
                            selectedImages.firstOrNull()
                        )
                    )

                    navHostController.navigateUp()
                }) {
                    Text(text = "완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B))
                }
            }
        )

        TextField(
            value = title, onValueChange = { title = it },
            textStyle = getTextStyle(textStyle = RememberTextStyle.HEAD_4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            placeholder = {
                Text(
                    text = "제목",
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_4).copy(color = fontHintColor)
                )
            },
            maxLines = 3
        )

        OutlinedTextField(
            value = content, onValueChange = { content = it },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            placeholder = {
                Text(
                    text = "그 날 친구와 무슨 추억을 쌓았나요?\n\n\n",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = fontHintColor),
                )
            },
            shape = RoundedCornerShape(size = 8.dp),
        )


        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImages = listOf(uri) }
        )

        fun launchPhotoPicker() {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        Button(
            onClick = { launchPhotoPicker() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            Text(
                text = "사진첨부",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (selectedImages.isNotEmpty()) {
            Card(
                Modifier
                    .height(180.dp)
                    .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = selectedImages.firstOrNull(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            selectedImages = emptyList()
                        }) {
                        Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = "cancel")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHistoryAddScreen() {
    HistoryAddScreen(rememberNavController())
}