package com.lee.remember.android

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun testScreen() {

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = {

            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .wrapContentHeight()
                .padding()
        ) {
            Text(
                "기념일",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = Color(0xFF1D1B20))
            )
            Icon(painter = painterResource(id = R.drawable.baseline_expand_more_24), contentDescription = "", tint = Color.Black)
        }

        TextField(
            value = "2023/11/10",
            onValueChange = {
//                date = it
            },
            readOnly = true,
            label = {
                Text("이벤트", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
            modifier = Modifier
                .fillMaxWidth(),
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

@Preview
@Composable
fun PreviewTestScreen() {
    testScreen()
}