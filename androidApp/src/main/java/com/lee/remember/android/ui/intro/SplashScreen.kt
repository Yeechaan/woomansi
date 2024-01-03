package com.lee.remember.android.ui.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.viewmodel.IntroViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: IntroViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    viewModel.initUserState()
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    scope.launch {
        viewModel.uiState.collectLatest { uiState ->
            if (uiState.isFirst != null || uiState.isAuthSuccess != null){
                if (uiState.isFirst == true) {
                    navController.navigate(RememberScreen.OnBoarding.name) {
                        popUpTo(RememberScreen.Splash.name) {
                            inclusive = true
                        }
                    }
                } else {
                    if (uiState.isAuthSuccess == true) {
                        navController.navigate(RememberScreen.History.name) {
                            popUpTo(RememberScreen.Splash.name) {
                                inclusive = true
                            }
                        }
                    } else {
                        navController.navigate(RememberScreen.Login.name) {
                            popUpTo(RememberScreen.Splash.name) {
                                inclusive = true
                            }
                        }
                    }
                }

                scope.cancel()
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(lightColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier.width(100.dp),
            painter = painterResource(id = R.drawable.ic_appbar), contentDescription = "logo"
        )
    }
}

@Preview
@Composable
fun PreviewSplash() {
    SplashScreen(rememberNavController())
}