package com.lee.remember.android.ui.common

import androidx.compose.foundation.Image
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.ui.RememberScreen
import com.lee.remember.android.ui.mainScreens

@Composable
fun RememberBottomNavigation(navController: NavHostController, currentScreen: String) {
    BottomNavigation(
        backgroundColor = Color.White,
    ) {
        mainScreens.forEach { mainScreen ->
            BottomNavigationItem(
                label = {
                    val title = when (mainScreen) {
                        RememberScreen.History.name -> stringResource(id = R.string.home)
                        RememberScreen.Memory.name -> stringResource(id = R.string.memory)
                        RememberScreen.Friend.name -> stringResource(id = R.string.friend)
                        else -> ""
                    }

                    Text(
                        title,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFF49454F))
                    )
                },
                icon = {
                    val resourceId = when (mainScreen) {
                        RememberScreen.History.name -> R.drawable.ic_home_off to R.drawable.ic_home_on
                        RememberScreen.Memory.name -> R.drawable.ic_feed_off to R.drawable.ic_feed_on
                        RememberScreen.Friend.name -> R.drawable.ic_friend_off to R.drawable.ic_friend_on
                        else -> R.drawable.ic_dot to R.drawable.ic_dot
                    }

                    if (currentScreen == mainScreen) {
                        Image(painterResource(id = resourceId.second), contentDescription = null)
                    } else {
                        Image(painterResource(id = resourceId.first), contentDescription = null)
                    }
                },
                selected = currentScreen == mainScreen,
                onClick = {
                    navController.popBackStack(RememberScreen.History.name, false)
                    navController.navigate(mainScreen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}