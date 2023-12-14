package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.ui.whiteColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavHostController) {
    val allCheckedState = remember { mutableStateOf(false) }
    val termsCheckedState = remember { mutableStateOf(false) }
    val privacyCheckedState = remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteColor),
    ) {
        TopAppBar(
            modifier = Modifier.shadow(elevation = 10.dp),
            title = { Text("", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
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

        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                .fillMaxSize(),
        ) {

            Text(
                modifier = Modifier.padding(top = 36.dp),
                text = "우만시 약관동의",
                style = getTextStyle(textStyle = RememberTextStyle.HEAD_2)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = allCheckedState.value,
                    onCheckedChange = {
                        termsCheckedState.value = it
                        privacyCheckedState.value = it

                        allCheckedState.value = it
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), uncheckedColor = Color.Black)
                )

                Text(modifier = Modifier.weight(1f), text = "모두 동의합니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))
            }

            Divider(modifier = Modifier.padding(vertical = 32.dp), color = Color.Black, thickness = 1.dp)

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsCheckedState.value,
                    onCheckedChange = {
                        allCheckedState.value = it && privacyCheckedState.value

                        termsCheckedState.value = it
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), uncheckedColor = Color.Black)
                )

                Text(modifier = Modifier.weight(1f), text = "[필수] 이용약관 동의", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))

                Image(
                    modifier = Modifier.clickable { localUriHandler.openUri("https://www.notion.so/620c795fbb904fb6a8d20a4c31e27e96") },
                    painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null
                )
            }

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = privacyCheckedState.value,
                    onCheckedChange = {
                        allCheckedState.value = it && termsCheckedState.value

                        privacyCheckedState.value = it
                    },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), uncheckedColor = Color.Black)
                )

                Text(modifier = Modifier.weight(1f), text = "[필수] 개인정보 처리방침", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))

                Image(
                    modifier = Modifier.clickable { localUriHandler.openUri("https://www.notion.so/39abfe07dd534610812ccfb5bea55212?pvs=4") },
                    painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null
                )
            }

            Button(
                onClick = {
                    if (allCheckedState.value) {
                        navController.navigate(RememberScreen.SignIn.name)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
                shape = RoundedCornerShape(size = 100.dp),
            ) {
                Text(
                    text = "동의하기",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.White),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }


}