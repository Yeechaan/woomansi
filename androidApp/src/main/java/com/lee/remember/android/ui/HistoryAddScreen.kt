package com.lee.remember.android.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import kotlinx.coroutines.launch

//0xFFD59519
val fontPointColor = Color(0xFFD59519)
val fontHintColor = Color(0x4D000000)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
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

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            textStyle = getTextStyle(textStyle = RememberTextStyle.HEAD_4),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
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
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            Text(
                text = "사진첨부",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 2.dp)
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

        val selectedFriends by rememberSaveable { mutableStateOf(mutableListOf<Contract>(Contract(
            id = "disputationi",
            name = "Conrad Forbes",
            number = "non",
            isChecked = false
        ))) }
        var isFriendEmpty by remember { mutableStateOf(true) }

        if (!isFriendEmpty) {
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
                    modifier = Modifier.padding(end = 8.dp)
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
                Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")

                selectedFriends.forEach {
                    InputChipExample(text = it.name) {
                        selectedFriends.remove(it)
                        if (selectedFriends.isEmpty()) {
                            isFriendEmpty = true
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            val contracts = listOf(
                Contract(id = "1", name = "Ollie Hardin", number = "1", isChecked = false),
                Contract(id = "2", name = "Ollie Hardin1", number = "2", isChecked = false),
                Contract(id = "3", name = "Ollie Hardin2", number = "3", isChecked = false),
                Contract(id = "4", name = "Ollie Hardin3", number = "4", isChecked = false),
                Contract(id = "5", name = "Ollie Hardin4", number = "5", isChecked = false),
                Contract(id = "6", name = "Ollie Hardin5", number = "6", isChecked = false),
                Contract(id = "7", name = "Ollie Hardin6", number = "7", isChecked = false),
            )

            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                LazyColumn(
                    Modifier.padding(start = 16.dp, end = 16.dp)
                ) {
                    items(contracts) { message ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = message.name,
                                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B)
                            )
                            Text(text = message.number, style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0x61000000)))

                            val checkedState = remember { mutableStateOf(false) }
                            Checkbox(
                                checked = checkedState.value,
                                onCheckedChange = {
                                    contracts.find { it.number == message.number }?.isChecked = it
                                    checkedState.value = it
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2C678), uncheckedColor = Color(0xFFF2C678))
                            )
                        }
                    }
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth().padding(bottom = 40.dp)
                        .background(Color(0xFFF8D393)),
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                selectedFriends.addAll(contracts.filter { it.isChecked })
                                if (selectedFriends.isNotEmpty()) {
                                    isFriendEmpty = false
                                }

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
    onDismiss: () -> Unit,
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
            onDismiss()
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
    HistoryAddScreen(rememberNavController())
}