package com.lee.remember.android.ui.my

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.lee.remember.android.R
import com.lee.remember.android.ui.pointColor
import com.lee.remember.android.ui.stringToBitmap
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.MyViewModel
import com.lee.remember.local.BaseRealm
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(
    navController: NavHostController,
    viewModel: MyViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val activity = (LocalContext.current as? Activity)

    if (uiState.value.success) {
        scope.launch {
            BaseRealm().delete()
            activity?.finish()
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
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {

        TopAppBar(
            modifier = Modifier.shadow(elevation = 10.dp),
            title = { Text("마이페이지", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            },
        )

        val user = uiState.value.user
        var id by remember { mutableStateOf("") }

//        HeadingLogoScreen(user?.name ?: "")

        Column(
            Modifier
                .shadow(elevation = 1.dp, spotColor = Color(0x36263E2B), ambientColor = Color(0x36263E2B))
                .shadow(elevation = 1.dp, spotColor = Color(0x33444444), ambientColor = Color(0x33444444))
                .fillMaxWidth()
                .background(color = Color.White)
                .padding(top = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .size(48.dp)
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
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(Color(0xff1D1B20)),
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }

            Text(
                user?.name ?: "",
                style = getTextStyle(textStyle = RememberTextStyle.HEAD_5),
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )

            Divider(color = pointColor, thickness = 8.dp)
        }

        Text("내 정보", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5), modifier = Modifier.padding(top = 24.dp, start = 16.dp))

        OutlinedTextField(
            value = user?.email ?: "", onValueChange = { id = it },
            label = { RememberTextField.label(text = "이메일") },
            placeholder = { RememberTextField.placeHolder(text = "이메일 입력") },
            textStyle = RememberTextField.textStyle(),
            colors = RememberTextField.colors(),
            readOnly = true,
            enabled = true,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

//        val scope = rememberCoroutineScope()
//        val activity = (LocalContext.current as? Activity)
        RememberFilledButton(text = "로그아웃") {
            scope.launch {
                // Todo 공통 코드 처리
                BaseRealm().delete()
                activity?.finish()

                scope.cancel()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            Modifier
                .background(pointColor)
                .fillMaxWidth(), horizontalAlignment = Alignment.End
        ) {
            TextButton(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(bottom = 32.dp),
                onClick = { viewModel.deleteUser() }) {
                Text(
                    text = "회원탈퇴",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_1).copy(Color(0xFF1D1B20)),
                    modifier = Modifier.drawBehind {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height - 1.sp.toPx()
                        drawLine(
                            color = Color(0xFF1D1B20),
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    }
                )
            }
        }


    }
}

@Composable
fun HeadingLogoScreen(name: String) {
    Column(
        Modifier
            .shadow(elevation = 1.dp, spotColor = Color(0x36263E2B), ambientColor = Color(0x36263E2B))
            .shadow(elevation = 1.dp, spotColor = Color(0x33444444), ambientColor = Color(0x33444444))
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(top = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "camera_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(pointColor)
        )

        Text(name, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5), modifier = Modifier.padding(top = 16.dp, bottom = 24.dp))

        Divider(color = pointColor, thickness = 8.dp)
    }
}

@Preview
@Composable
fun PreviewMyScreen() {
    MyScreen(rememberNavController())
}