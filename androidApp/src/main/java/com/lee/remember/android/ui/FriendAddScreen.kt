package com.lee.remember.android.ui

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.utils.rememberImeState
import com.lee.remember.remote.FriendApi
import com.lee.remember.remote.request.FriendRequest
import com.lee.remember.repository.FriendRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendAddScreen(navHostController: NavHostController, name: String?, number: String?) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    var savedImage by remember { mutableStateOf("") }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                selectedImage = it
                savedImage = ""
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
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var name by remember { mutableStateOf(name ?: "") }
        var group by remember { mutableStateOf("") }
        var number by remember { mutableStateOf(number ?: "") }
        var dateTitle by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }

        val state = rememberDatePickerState()
        state.selectedDateMillis?.let {
            date = convertMillisToDate(it)
        }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()


        val apiScope = rememberCoroutineScope()

        TopAppBar(
            modifier = Modifier.shadow(elevation = 1.dp),
            title = { Text("추가", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            actions = {
                Text(
                    "완료",
                    Modifier
                        .padding(end = 12.dp)
                        .clickable {
                            apiScope.launch() {
//                    try {
                                var profileImage = ""
                                selectedImage?.let {
                                    val bitmap = if (Build.VERSION.SDK_INT >= 28) {
                                        val source = ImageDecoder.createSource(context.contentResolver, it)
                                        ImageDecoder.decodeBitmap(source)
                                    } else {
                                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                                    }

                                    withContext(Dispatchers.IO) {
//                            val quality = 50
//                            val scaleDownBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * quality).toInt(), (bitmap.height * quality).toInt(), true)
                                        profileImage = bitmapToString(bitmap)
                                    }

//                        profileImage = bitmapToString(bitmap)
                                }

//                    profileImage = test(context, selectedImage)
                                Napier.d("@@@ui ${profileImage.length}")

//                        selectedImage?.let {
//                            saveImageToInternalStorage(context, it)
//                        }

                                val friendRequest = FriendRequest(
                                    name = name,
                                    phoneNumber = number,
                                    description = "",
                                    events = listOf(FriendRequest.Event(dateTitle, date)),
                                    profileImage = profileImage
                                )

                                val response = FriendApi().addFriend(accessToken, listOf(friendRequest))

                                if (response != null && response.resultCode == "SUCCESS") {
                                    FriendRepository().fetchFriends()

                                    Napier.d("### ${response.resultCode}")

                                    // @@@
                                    response.result?.map {
                                        val size = it.profileImage?.image?.length
                                        Napier.d("@@@addFriend ${it.id} / $size")
                                    }

                                    navHostController.navigateUp()
                                } else {
                                    Toast
                                        .makeText(context, "Internal Server Error", Toast.LENGTH_SHORT)
                                        .show()
                                }
//                    } catch (e: Exception) {
//                        e.localizedMessage ?: "error"
//                    }
                            }
                        },
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF33322E)),
                )
            },
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xffEFEEEC))
                .clickable { launchPhotoPicker() },
            contentAlignment = Alignment.Center
        ) {
            if (savedImage.isNotEmpty()) {
                val bitmap: Bitmap? = stringToBitmap(savedImage)
                bitmap?.let {
                    Image(
                        bitmap = bitmap.asImageBitmap(), contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else if (selectedImage != null) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_camera_32),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(Color(0xff1D1B20)),
                    modifier = Modifier.padding(40.dp),
                )
            }
        }

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
        ) {
            Text("필수 입력", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0x61000000)))

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { RememberTextField.label(text = "이름") },
                textStyle = RememberTextField.textStyle(),
                colors = RememberTextField.colors(),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()

            OutlinedTextField(
                value = number, onValueChange = { number = it },
                label = { RememberTextField.label(text = "연락처") },
                textStyle = RememberTextField.textStyle(),
                colors = RememberTextField.colors(),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    if (isFocused && number.isNotEmpty()) {
                        IconButton(onClick = { number = "" }) {
                            Icon(painter = painterResource(id = R.drawable.ic_cancel), contentDescription = "Clear")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                interactionSource = interactionSource
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "선택 입력",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0x61000000))
            )

//            Box(
//                modifier = Modifier
//                    .wrapContentSize(Alignment.TopStart)
//            ) {
//                var expanded by remember { mutableStateOf(false) }
//                val items = listOf("가족", "가장 친한", "친해지고 싶은")
//                var selectedIndex by remember { mutableStateOf<Int?>(null) }
//
//                OutlinedTextField(
//                    value = selectedIndex?.let { items[it] } ?: group, onValueChange = { group = it }, readOnly = true,
//                    label = { RememberTextField.label(text = "그룹") },
//                    textStyle = RememberTextField.textStyle(),
//                    colors = RememberTextField.colors(),
//                    singleLine = true,
//                    modifier = Modifier
//                        .padding(top = 12.dp)
//                        .fillMaxWidth(),
//                    trailingIcon = {
//                        IconButton(onClick = { expanded = true }) {
//                            if (!expanded) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.baseline_expand_more_24),
//                                    contentDescription = "",
//                                    tint = Color.Black
//                                )
//                            } else {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.baseline_expand_less_24),
//                                    contentDescription = "",
//                                    tint = Color.Black
//                                )
//                            }
//                        }
//                    }
//                )
//
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(Color.White)
//                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
//                ) {
//                    DropdownMenuItem(text = {
//                        Text(text = "새 그룹 추가")
//                    }, onClick = {
//                        navHostController.navigate(RememberScreen.FriendGroup.name)
//                        expanded = false
//                    })
//
//                    items.forEachIndexed { index, s ->
//                        DropdownMenuItem(text = {
//                            Text(text = s)
//                        }, onClick = {
//                            selectedIndex = index
//                            group = items[index]
//                            expanded = false
//                        })
//                    }
//                }
//            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 12.dp)) {
                var expanded by remember { mutableStateOf(false) }
                val items = listOf("생일", "기념일", "기일", "기타")
                var selectedIndex by remember { mutableStateOf(0) }

                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .padding()
                ) {
                    Text(
                        items[selectedIndex],
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = Color(0xFF1D1B20))
                    )
                    if (!expanded) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_expand_more_24),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_expand_less_24),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(text = {
                            Text(text = s)
                        }, onClick = {
                            selectedIndex = index
                            dateTitle = items[index]
                            expanded = false
                        })
                    }
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
                    label = { RememberTextField.label(text = "날짜 선택") },
                    textStyle = RememberTextField.textStyle(),
                    colors = RememberTextField.colors(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
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
            }
        }

//        Spacer(modifier = Modifier.weight(1f))
    }
}