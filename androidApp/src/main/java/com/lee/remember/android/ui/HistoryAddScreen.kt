package com.lee.remember.android.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.accessToken
import com.lee.remember.android.bottomPadding
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.local.dao.FriendDao
import com.lee.remember.local.dao.MemoryDao
import com.lee.remember.local.model.MemoryRealm
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.MemoryApi
import com.lee.remember.request.MemoryRequest
import io.github.aakira.napier.Napier
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

//0xFFD59519
val fontPointColor = Color(0xFFD59519)
val fontHintColor = Color(0x4D000000)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HistoryAddScreen(navHostController: NavHostController, friendId: String?) {
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(null) {
        try {
            val response = FriendApi().getFriend(accessToken, friendId ?: "")

            if (response != null) {
//                Napier.d("###hi ${response}")

                response.result?.let {
                    name = it.name
                    phoneNumber = it.phoneNumber ?: ""
                }

                response.toString()
            }
        } catch (e: Exception) {
            Napier.d("### ${e.localizedMessage}")
            e.localizedMessage ?: "error"
        }
    }

    val friends by rememberSaveable {
        mutableStateOf(
            FriendDao().getFriends().map {
                Contract(id = it.id.toString(), name = it.name, number = it.phoneNumber, isChecked = false)
            }.toMutableList()
        )
    }

    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImage = uri }
    )

    fun launchPhotoPicker() {
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        var title by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }

        TopAppBar(
            modifier = Modifier.shadow(10.dp),
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
            actions = {
                TextButton(onClick = {
                    scope.launch {
                        val image = uriToBitmapString(context, selectedImage)

                        // remote
                        val request = MemoryRequest(
                            title = title,
                            description = content,
                            date = "2023-12-18",
                            friendIds = friends.filter { it.isChecked }.map { it.id.toInt() },
                            images = listOf(image)
                        )

                        val response = MemoryApi().addMemory(accessToken, request)

                        // Todo remote 실패한 경우 local 저장 여부
                        // local
                        val memoryDao = MemoryDao()
                        val memoryRealm = MemoryRealm().apply {
                            this.title = title
                            this.description = content
                            this.date = "2023-12-18"
                            this.friendTags = friends.filter { it.isChecked }.map { it.name }.toRealmList()
                            this.images.add(image)
                        }
                        if (response != null && friendId != null) {
                            memoryDao.setMemoryByFriendId(friendId.toInt(), memoryRealm.apply { this.id = response.result?.id ?: -1 })
                        } else {
                            memoryDao.setMemoryByPhoneNumber(phoneNumber, memoryRealm)
                        }

                        navHostController.navigateUp()
                    }

                }) {
                    Text(text = "완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF49454F)))
                }
            }
        )

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2B),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, start = 16.dp, end = 16.dp),
            placeholder = {
                Text(
                    text = "내 친구와 있었던 추억",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = fontHintColor)
                )
            },
            label = {
                Text(
                    "제목",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = fontHintColor),
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
            label = {
                Text(
                    "내용",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = fontHintColor),
                )
            },
            shape = RoundedCornerShape(size = 8.dp),
        )

        Button(
            onClick = { launchPhotoPicker() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
            shape = RoundedCornerShape(size = 100.dp),
        ) {
            Text(
                text = "사진첨부",
                modifier = Modifier.padding(vertical = 2.dp),
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White)
            )
        }

        if (selectedImage != null) {
            Card(
                Modifier
                    .height(180.dp)
                    .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = selectedImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = {
                            selectedImage = null
                        }) {
                        Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = "cancel", tint = Color.White)
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp),
            text = "관련된 친구", style = getTextStyle(textStyle = RememberTextStyle.BODY_4B)
        )

        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }

        var isFriendEmpty by remember { mutableStateOf(true) }

        if (isFriendEmpty) {
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(size = 8.dp),
                border = BorderStroke(1.dp, Color(0xFF79747E))
            ) {
                Text(
                    text = "친구 찾기",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_4B).copy(Color(0xFF79747E)),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .padding(horizontal = 6.dp)
                )
                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "", tint = Color.Black)
            }
        } else {
            val rowScrollState = rememberScrollState()
            Row(
                Modifier
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp)
                    .horizontalScroll(rowScrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(modifier = Modifier.clickable {
                    showBottomSheet = true
                }, painter = painterResource(id = R.drawable.ic_add), contentDescription = "")

                friends.filter { it.isChecked }.forEach {
                    InputChipExample(text = it.name) {
                        // 친구 삭제 시
                        it.isChecked = false
                        isFriendEmpty = friends.none { it.isChecked }
                    }
                }
            }
        }

        if (showBottomSheet) {
            val tempFriends = mutableListOf<Contract>()
            tempFriends.clear()
            tempFriends.addAll(friends)

            ModalBottomSheet(
                modifier = Modifier,
                containerColor = Color.White,
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                LazyColumn(
                    Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    items(tempFriends) { message ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = message.name,
                                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B)
                            )
                            Text(text = message.number, style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0x61000000)))

                            val checkedState = remember { mutableStateOf(message.isChecked) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = {
                                    tempFriends.find { it.number == message.number }?.isChecked = it
                                    checkedState.value = it
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), uncheckedColor = Color.Black)
                            )
                        }
                    }
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 40.dp)
                        .background(Color(0xFFF2BE2F)),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                friends.clear()
                                friends.addAll(tempFriends)
                                isFriendEmpty = friends.none { it.isChecked }

                                showBottomSheet = false
                            }
                        }
                    }) {
                    Text(
                        text = "선택완료",
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF50432E))
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputChipExample(
    text: String,
    onClick: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        modifier = Modifier.padding(start = 8.dp),
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = Color(0xFFF8F1DE),
            containerColor = Color(0xFFF8F1DE),
            labelColor = Color.White
        ),
        onClick = {
            onClick()
            enabled = !enabled
        },
        label = { Text(text, style = getTextStyle(textStyle = RememberTextStyle.BODY_4B)) },
        selected = enabled,
        leadingIcon = { Image(painterResource(id = R.drawable.ic_user), contentDescription = "Localized description") },
        trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Localized description") },
    )
}

@Preview
@Composable
fun PreviewHistoryAddScreen() {
    HistoryAddScreen(rememberNavController(), null)
}