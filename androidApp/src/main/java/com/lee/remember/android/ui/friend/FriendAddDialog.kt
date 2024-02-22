package com.lee.remember.android.ui.friend

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@Composable
fun FriendAddDialog(
    onDismissRequest: () -> Unit,
    onNewClicked: () -> Unit,
    onContactsClicked: () -> Unit,
) {
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
            ) {
                Text(
                    text = "친구 추가",
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_3).copy(fontColorBlack),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "어떤 방식으로 친구를 추가 하시겠어요?",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color(0x94000000)),
                    modifier = Modifier.padding(top = 16.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onContactsClicked,
                    ) {
                        Text(
                            "연락처로 추가",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(fontColorBlack),
                        )
                    }

                    Button(
                        onClick = onNewClicked,
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x1FF2C678)),
                        shape = RoundedCornerShape(size = 100.dp),
                    ) {
                        Text(
                            "새 친구 등록",
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
fun PreviewFriendAddDialog() {
    FriendAddDialog({}, {}, {})
}