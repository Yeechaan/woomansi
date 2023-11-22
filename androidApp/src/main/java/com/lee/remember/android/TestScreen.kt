package com.lee.remember.android

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun testScreen() {
    val friends = listOf("harry", "Jay", "John", "harry", "Jay", "John","harry", "Jay", "John")

    LazyRow(
        Modifier
            .padding(top = 24.dp, start = 16.dp, end = 16.dp)
    ) {
        items(friends) {
            InputChipExample(text = it) {

            }
        }
    }

//    Row {
//
//        friends.forEach {
//            InputChipExample(text = it) {
//
//            }
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputChipExample(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        modifier = Modifier.padding(start = 8.dp),
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = Color(0xFFF8F1DE),
            containerColor = Color(0xFFF8F1DE),
            labelColor = Color.White
        ),
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text, style = getTextStyle(textStyle = RememberTextStyle.BODY_4B)) },
        selected = enabled,
        leadingIcon = { Icon(painterResource(id = R.drawable.baseline_account_circle_24), contentDescription = "Localized description") },
        trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Localized description") },
    )
}

@Preview
@Composable
fun PreviewTestScreen() {
    testScreen()
}