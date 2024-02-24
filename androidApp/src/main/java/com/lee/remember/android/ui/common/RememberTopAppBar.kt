package com.lee.remember.android.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberTopAppBarMain(navHostController: NavHostController) {
    TopAppBar(
        modifier = Modifier.shadow(2.dp),
        title = {},
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
        navigationIcon = {
            Icon(
                modifier = Modifier.padding(start = 16.dp),
                painter = painterResource(id = R.drawable.ic_appbar),
                contentDescription = stringResource(R.string.back_button)
            )
        },
        actions = {
            IconButton(onClick = {
                navHostController.navigate(RememberScreen.My.name)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_account), contentDescription = "")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberTopAppBar(navHostController: NavHostController, title: String, actions: @Composable RowScope.() -> Unit = {}) {
    TopAppBar(
        modifier = Modifier.shadow(2.dp),
        title = { Text(title, style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
        navigationIcon = {
            IconButton(onClick = {
                navHostController.navigateUp()
            }) {
                Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "")
            }
        },
        actions = actions
    )
}