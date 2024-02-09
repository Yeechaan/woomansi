package com.lee.remember.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lee.remember.android.R
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

sealed class ContactType {
    object Call : ContactType()
    object Message : ContactType()
}

@Composable
fun FriendContactDialog(
    name: String,
    contactType: ContactType,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    var title = ""
    var descriptions = ""
    var buttonConfirm = ""
    var imageResource = -1

    when (contactType) {
        ContactType.Call -> {
            title = "전화"
            descriptions = "안심하세요!\n바로 통화로 연결되지 않아요."
            buttonConfirm = "전화하기"
            imageResource = R.drawable.ic_call
        }

        ContactType.Message -> {
            title = "문자"
            descriptions = "친구에게 문자를 보내보세요."
            buttonConfirm = "문자하기"
            imageResource = R.drawable.ic_message
        }
    }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_3).copy(fontColorBlack),
                    modifier = Modifier.fillMaxWidth(),
                )
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = descriptions,
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color(0xFF000000)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(
                    text = name,
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(Color(0xFFE5AA12)),
                    modifier = Modifier.padding(top = 32.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text(
                            "취소하기",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(fontColorBlack),
                        )
                    }

                    Button(
                        onClick = onConfirm,
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x1FF2C678)),
                        shape = RoundedCornerShape(size = 100.dp),
                    ) {
                        Text(
                            buttonConfirm,
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(fontColorBlack),
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewFriendContactDialog() {
    FriendContactDialog("111", ContactType.Call, {}, {})
}