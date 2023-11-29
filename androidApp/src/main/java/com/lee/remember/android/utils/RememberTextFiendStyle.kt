package com.lee.remember.android.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.lee.remember.android.ui.fontColorPoint


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberTextFieldStyle() = TextFieldDefaults.outlinedTextFieldColors(
    errorBorderColor = Color(0xFFB3661E),
    errorLabelColor = Color(0xFFB3661E),
    focusedBorderColor = fontColorPoint,
    focusedLabelColor = fontColorPoint,
    cursorColor = fontColorPoint,
)
