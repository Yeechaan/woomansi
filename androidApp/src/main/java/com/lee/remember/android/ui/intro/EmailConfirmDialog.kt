package com.lee.remember.android.ui.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lee.remember.android.ui.friend.fontColorBlack
import com.lee.remember.android.ui.common.RememberTextField
import com.lee.remember.android.ui.common.RememberTextStyle
import com.lee.remember.android.ui.common.getTextStyle
import kotlinx.coroutines.launch

@Composable
fun EmailConfirmDialog(
    snackbarHostState: SnackbarHostState,
    emailCode: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Boolean) -> Unit,
) {
    var emailCodeConfirm by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = { onDismissRequest() }) {
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
                    text = "인증번호 입력",
                    style = getTextStyle(textStyle = RememberTextStyle.HEAD_3).copy(fontColorBlack),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "이메일로 인증번호를 보냈어요.",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color(0x94000000)),
                    modifier = Modifier.padding(top = 16.dp),
                )

                OutlinedTextField(
                    value = emailCodeConfirm,
                    onValueChange = {
                        if (it.length <= 6) {
                            emailCodeConfirm = it
                        }
                    },
                    label = { RememberTextField.label(text = "인증번호") },
                    placeholder = { RememberTextField.placeHolder(text = "6자리") },
                    textStyle = RememberTextField.textStyle(),
                    colors = RememberTextField.colors(),
                    singleLine = true,
                    minLines = 1,
                    enabled = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
//                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            "취소",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(fontColorBlack),
                        )
                    }
                    TextButton(
                        onClick = {
                            if (emailCodeConfirm.isNotEmpty() && emailCodeConfirm == emailCode) {
                                onConfirmation(true)
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("인증번호가 일치하지 않습니다.")
                                }
                            }
                        },
                    ) {
                        Text(
                            "확인",
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
fun PreviewEmailConfirmDialog() {
    EmailConfirmDialog(SnackbarHostState(), "", {}, {})
}