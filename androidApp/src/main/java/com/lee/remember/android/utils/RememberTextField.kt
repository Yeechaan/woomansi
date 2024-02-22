package com.lee.remember.android.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.lee.remember.android.ui.friend.fontColorPoint
import com.lee.remember.android.ui.memory.fontHintColor

object RememberTextField {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors() = TextFieldDefaults.outlinedTextFieldColors(
        errorBorderColor = Color(0xFFD59519),
        errorLabelColor = Color(0xFFD59519),
        focusedBorderColor = fontColorPoint,
        focusedLabelColor = fontColorPoint,
        cursorColor = fontColorPoint,
    )

    @Composable
    fun label(text: String) =
        Text(text, style = getTextStyle(textStyle = RememberTextStyle.BODY_4))

    @Composable
    fun placeHolder(text: String) =
        Text(text, style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(fontHintColor))

    @Composable
    fun textStyle() =
        getTextStyle(textStyle = RememberTextStyle.BODY_2)
}
