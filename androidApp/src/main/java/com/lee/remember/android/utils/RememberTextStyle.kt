package com.lee.remember.android.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class RememberTextStyle {
    HEAD_1,     // 38sp
    HEAD_2,     // 28sp
    HEAD_3,     // 24sp
    HEAD_4,     // 20sp
    HEAD_5,     // 18sp

    BODY_1B,    // 16sp
    BODY_1,
    BODY_2B,    // 14sp
    BODY_2,
    BODY_3B,    // 13sp
    BODY_3,
    BODY_4B,    // 12sp
    BODY_4
}

@Composable
fun getTextStyle(textStyle: RememberTextStyle): TextStyle {
    return when (textStyle) {
        RememberTextStyle.HEAD_1 -> TextStyle(fontSize = 38.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.HEAD_2 -> TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.HEAD_3 -> TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.HEAD_4 -> TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.HEAD_5 -> TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)

        RememberTextStyle.BODY_1B -> TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.BODY_1 -> TextStyle(fontSize = 16.sp)
        RememberTextStyle.BODY_2B -> TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.BODY_2 -> TextStyle(fontSize = 14.sp)
        RememberTextStyle.BODY_3B -> TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.BODY_3 -> TextStyle(fontSize = 13.sp)
        RememberTextStyle.BODY_4B -> TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        RememberTextStyle.BODY_4 -> TextStyle(fontSize = 12.sp)
    }
}