package com.lee.remember.android.utils

import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RememberCheckbox() = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), checkmarkColor = Color(0x94000000), uncheckedColor = Color.Black)