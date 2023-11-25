package com.lee.remember.android.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.GreetingKtor
import com.lee.remember.android.GreetingView
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    Column() {
        var id by remember { mutableStateOf("asd@test.com") }
        var password by remember { mutableStateOf("password") }

        // Ktor Test
        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("Loading") }
        LaunchedEffect(true) {
            scope.launch {
                text = try {
                    GreetingKtor().greeting(id, password)
                } catch (e: Exception) {
                    e.localizedMessage ?: "error"
                }

//                Log.d("####", text)
            }
        }
        GreetingView(text)

        TextField(
            value = id, onValueChange = { id = it },
            label = {
                Text("id", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        TextField(
            value = password, onValueChange = { password = it },
            label = {
                Text("password", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
            },
            textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        )

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                navController.navigate(RememberScreen.SelectContact.name)
            }) {
            Text(text = "Login")
        }
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}