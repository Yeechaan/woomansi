package com.lee.remember.android.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RememberFilledButton(
    text: String,
    horizontalPaddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    verticalPaddingValues: PaddingValues = PaddingValues(top = 16.dp, bottom = 32.dp),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontalPaddingValues)
            .padding(verticalPaddingValues),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2BE2F)),
        shape = RoundedCornerShape(size = 100.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 2.dp),
            style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0x94000000))
        )
    }
}

@Composable
fun RememberOutlinedButton(
    text: String,
    paddingValues: PaddingValues = PaddingValues(top = 16.dp, bottom = 32.dp),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(paddingValues),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(size = 100.dp),
        border = BorderStroke(1.dp, Color(0xFFF2BE2F))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 2.dp),
            style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFFD59519))
        )
    }
}