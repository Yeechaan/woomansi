package com.lee.remember.android.ui.memory

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.ui.common.RememberTopAppBar
import com.lee.remember.android.ui.friend.convertMillisToDate
import com.lee.remember.android.ui.friend.uriToBitmapString
import com.lee.remember.android.utils.RememberCheckbox
import com.lee.remember.android.utils.RememberOutlinedButton
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextField.placeHolder
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.MemoryAddViewModel
import com.lee.remember.remote.request.MemoryRequest
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Calendar

//0xFFD59519
val fontPointColor = Color(0xFFD59519)
val fontHintColor = Color(0x4D000000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryAddScreen(
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    friendId: String?,
    viewModel: MemoryAddViewModel = koinViewModel(parameters = { parametersOf(friendId?.toInt()) }),
) {
    val uiState by viewModel.uiState.collectAsState()

//    when(uiState) {
//        is MemoryAddUiState.Initialize -> { }
//        MemoryAddUiState.Loading -> { }
//        MemoryAddUiState.Success -> {
//            viewModel.resetUiState()
//            navHostController.navigateUp()
//        }
//        is MemoryAddUiState.Error -> { }
//    }

    if (uiState.isSuccess) {
        navHostController.navigateUp()
        viewModel.resetUiState()
    }
    if (uiState.message.isNotEmpty()) {
        Napier.d(uiState.message)
    }

    val scrollState = rememberScrollState()

    val today = convertMillisToDate(Calendar.getInstance().timeInMillis)
    var date by remember { mutableStateOf(today) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val friends by rememberSaveable {
//        mutableStateOf((uiState as MemoryAddUiState.Initialize).contracts.toMutableList())
        mutableStateOf(uiState.contracts.toMutableList())
    }

    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                selectedImage = uri
            }
        }
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


        RememberTopAppBar(
            navHostController = navHostController,
            title = uiState.name,
            actions = {
                TextButton(onClick = {
                    if (title.isEmpty()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("제목을 입력해 주세요")
                        }
                        return@TextButton
                    }

                    val bitmapImage = uriToBitmapString(context, selectedImage)

                    // remote
                    val addedFriends = mutableListOf(friendId?.toInt() ?: -1)
                    friends.filter { it.isChecked }.map {
                        addedFriends.add(it.id.toInt())
                    }

                    val request = MemoryRequest(
                        title = title,
                        description = content,
                        date = date,
                        friendIds = addedFriends,
                        images = listOf(MemoryRequest.Image(bitmapImage))
                    )

                    viewModel.addMemory(request)
                }) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            color = Color(0xFFF2BE2F),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    } else {
                        Text(text = "완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF49454F)))
                    }
                }
            }
        )

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { RememberTextField.label(text = "제목") },
            placeholder = { placeHolder("내 친구와 있었던 추억") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 28.dp, start = 16.dp, end = 16.dp),
            maxLines = 3
        )

        OutlinedTextField(
            value = content, onValueChange = { content = it },
            label = { RememberTextField.label(text = "내용") },
            placeholder = { placeHolder("그 날 친구와 무슨 추억을 쌓았나요?") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        )


        val state = rememberDatePickerState()
        state.selectedDateMillis?.let {
            date = convertMillisToDate(it)
        }

        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            DatePickerDialog(
                onDismissRequest = { openDialog.value = false },
                confirmButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("확인", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFF33322E)))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }
                    ) {
                        Text("취소", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFFD59519)))
                    }
                },
                colors = DatePickerDefaults.colors(containerColor = Color.White)
            ) { DatePicker(state = state) }
        }

        OutlinedTextField(
            value = date, onValueChange = { date = it }, readOnly = true,
            label = { RememberTextField.label(text = "추억 날짜") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp),
            trailingIcon = {
                IconButton(
                    onClick = { openDialog.value = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calander),
                        contentDescription = "Clear"
                    )
                }
            }
        )

        RememberOutlinedButton(text = "사진첨부", verticalPaddingValues = PaddingValues(top = 16.dp, bottom = 0.dp), onClick = {
            launchPhotoPicker()
        })

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
        var showBottomSheet by remember { mutableStateOf(false) }

        var isFriendEmpty by remember { mutableStateOf(true) }

        if (isFriendEmpty) {
            Button(
                onClick = { showBottomSheet = true },
                modifier = Modifier
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(size = 8.dp),
                border = BorderStroke(1.dp, Color(0xFF79747E))
            ) {
                Text(
                    text = "친구 찾기",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_4B).copy(Color(0xFF79747E)),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .padding(horizontal = 4.dp)
                )
                Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "", tint = Color.Black)
            }
        } else {
            val rowScrollState = rememberScrollState()
            Row(
                Modifier
                    .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 48.dp)
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
                                colors = RememberCheckbox()
                            )
                        }
                    }
                }

                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 56.dp)
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
fun PreviewMemoryAddScreen() {
    MemoryAddScreen(rememberNavController(), SnackbarHostState(), null)
}