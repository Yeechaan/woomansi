package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.ui.common.RememberTopAppBar
import com.lee.remember.android.ui.friend.whiteColor
import com.lee.remember.android.utils.RememberCheckbox
import com.lee.remember.android.utils.RememberFilledButton
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(navController: NavHostController) {
    var allCheckedState by remember { mutableStateOf(false) }
    var termsCheckedState by remember { mutableStateOf(false) }
    var privacyCheckedState by remember { mutableStateOf(false) }
    val localUriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteColor),
    ) {
        RememberTopAppBar(navHostController = navController, title = "")

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
                    checked = allCheckedState,
                    onCheckedChange = {
                        termsCheckedState = it
                        privacyCheckedState = it

                        allCheckedState = it
                    },
                    colors = RememberCheckbox()
                )

                Text(modifier = Modifier.weight(1f), text = "모두 동의합니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))
            }

            Divider(modifier = Modifier.padding(vertical = 32.dp), color = Color.Black, thickness = 1.dp)

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsCheckedState,
                    onCheckedChange = {
                        allCheckedState = it && privacyCheckedState

                        termsCheckedState = it
                    },
                    colors = RememberCheckbox()
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
                    checked = privacyCheckedState,
                    onCheckedChange = {
                        allCheckedState = it && termsCheckedState

                        privacyCheckedState = it
                    },
                    colors = RememberCheckbox()
                )

                Text(modifier = Modifier.weight(1f), text = "[필수] 개인정보 처리방침", style = getTextStyle(textStyle = RememberTextStyle.BODY_1B))

                Image(
                    modifier = Modifier.clickable { localUriHandler.openUri("https://www.notion.so/39abfe07dd534610812ccfb5bea55212?pvs=4") },
                    painter = painterResource(id = R.drawable.ic_arrow_right), contentDescription = null
                )
            }

            RememberFilledButton(text = "동의하기", horizontalPaddingValues = PaddingValues(horizontal = 0.dp), onClick = {
                if (allCheckedState) {
                    navController.navigate(RememberScreen.SignUp.name)
                }
            })
        }
    }


}

@Preview
@Composable
fun PreviewTermsScreen() {
    TermsScreen(rememberNavController())
}