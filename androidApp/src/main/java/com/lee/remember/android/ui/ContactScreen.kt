package com.lee.remember.android.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController

@Composable
fun ContactScreen(navHostController: NavHostController) {
    Text(
        text = "ContactScreen",
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
    )
}