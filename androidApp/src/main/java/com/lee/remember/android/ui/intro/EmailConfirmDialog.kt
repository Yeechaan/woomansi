package com.lee.remember.android.ui.intro

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lee.remember.android.ui.fontColorBlack
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.remote.AuthApi
import com.lee.remember.request.EmailCheckRequest
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@Composable
fun EmailConfirmDialog(
    email: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (Boolean) -> Unit,
) {
    var emailCode by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                    value = emailCode,
                    onValueChange = {
                        if (it.length <= 6) {
                            emailCode = it
                        }
                    },
                    label = { RememberTextField.label(text = "인증번호") },
                    placeholder = { RememberTextField.placeHolder(text = "6자리") },
                    textStyle = RememberTextField.textStyle(),
                    colors = RememberTextField.colors(),
                    singleLine = true,
                    minLines = 1,
                    enabled = true,
//                    isError = !isValid && emailCode.isNotEmpty(),
//                    supportingText = {
//                        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
//                        isValid = emailCode.matches(emailRegex.toRegex())
//
//                        if (!isValid && emailCode.isNotEmpty()) {
//                            Text("이메일 형식이 올바르지 않습니다.", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFFB3661E)))
//                        }
//                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
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
                            scope.launch {
                                val response = AuthApi().checkEmailCode(EmailCheckRequest(email, emailCode))

                                // Todo 서버 로직 확인
//                                if (response != null && response.resultCode == "SUCCESS" && response.result?.result == true) {
                                if (response != null && response.resultCode == "SUCCESS") {
                                    onConfirmation(true)
                                } else {
                                    Toast.makeText(context, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
//                        modifier = Modifier.padding(8.dp),
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
    EmailConfirmDialog("", {}, {})
}