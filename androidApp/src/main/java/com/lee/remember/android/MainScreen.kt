package com.lee.remember.android

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.ui.ContactScreen
import com.lee.remember.android.ui.FeedScreen
import com.lee.remember.android.ui.FriendEditScreen
import com.lee.remember.android.ui.FriendHistoryScreen
import com.lee.remember.android.ui.FriendProfileScreen
import com.lee.remember.android.ui.FriendScreen
import com.lee.remember.android.ui.HistoryAddScreen
import com.lee.remember.android.ui.HistoryScreen
import com.lee.remember.android.ui.LoginScreen
import com.lee.remember.android.ui.SelectContractScreen
import com.lee.remember.android.ui.SignInScreen
import com.lee.remember.android.ui.fontPointColor
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

enum class RememberScreen(@StringRes val title: Int) {
    Splash(title = R.string.splash),
    SignIn(title = R.string.sign_in),
    Login(title = R.string.login),
    SelectContact(title = R.string.select_contact),
    Contact(title = R.string.select_contact),
    History(title = R.string.history),
    Friend(title = R.string.friend),
    Feed(title = R.string.feed),
    FriendProfile(title = R.string.select_contact),
    FriendHistory(title = R.string.friend_history),
    FriendEdit(title = R.string.friend_edit),
    HistoryAdd(title = R.string.history_add)
}

val mainScreens = listOf(RememberScreen.History, RememberScreen.Feed, RememberScreen.Friend)

var userId: Int = -1
var accessToken: String = ""
val friendProfiles = mutableListOf<FriendProfile>()
var selectedFriendPhoneNumber = ""

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = RememberScreen.valueOf(
        backStackEntry?.destination?.route ?: RememberScreen.Splash.name
    )

    val bottomBarState = rememberSaveable {
        (mutableStateOf(mainScreens.contains(currentScreen)))
    }

    Scaffold(
        topBar = {
//            MainAppBar(
//                currentScreen = currentScreen,
//                canNavigateBack = navController.previousBackStackEntry != null,
//                navigateUp = { navController.navigateUp() }
//            )
        },
        bottomBar = {

            if (mainScreens.contains(currentScreen)) {

                BottomNavigation(
                    backgroundColor = Color.White
                ) {
                    mainScreens.forEach { mainScreen ->
                        BottomNavigationItem(
                            label = { Text(stringResource(id = mainScreen.title), fontSize = 14.sp) },
                            icon = {
                                Icon(
                                    painterResource(id = R.drawable.ic_dot),
                                    contentDescription = stringResource(R.string.back_button)
                                )
                            },
                            selected = currentScreen == mainScreen,
                            onClick = {
                                navController.navigate(mainScreen.name) {
                                    navController.graph.startDestinationRoute?.let {
                                        popUpTo(it) { saveState = true }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RememberScreen.Splash.name,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable(route = RememberScreen.Splash.name) {
                SplashScreen(navController)
            }
            composable(route = RememberScreen.SignIn.name) {
                SignInScreen(navController)
            }
            composable(route = RememberScreen.Login.name) {
                LoginScreen(navController)
            }
            composable(route = RememberScreen.SelectContact.name) {
                SelectContractScreen(navController)
            }
            composable(route = RememberScreen.Contact.name) {
                ContactScreen(navController)
            }
            composable(route = RememberScreen.History.name) {
                HistoryScreen(navController)
            }
            composable(route = RememberScreen.Friend.name) {
                FriendScreen(navController)
            }
            composable(route = RememberScreen.Feed.name) {
                FeedScreen(navController)
            }
            composable(route = RememberScreen.FriendProfile.name) {
                FriendProfileScreen(navHostController = navController)
            }
            composable(route = RememberScreen.FriendHistory.name) {
                FriendHistoryScreen(navHostController = navController)
            }
            composable(route = RememberScreen.FriendEdit.name) {
                FriendEditScreen(navHostController = navController)
            }
            composable(route = RememberScreen.HistoryAdd.name) {
                HistoryAddScreen(navHostController = navController)
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_app), contentDescription = "logo",
            modifier = Modifier
                .width(100.dp)
                .padding(top = 100.dp)
        )

        Text(
            text = "우리가 만난 시절",
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            style = getTextStyle(textStyle = RememberTextStyle.BODY_4),
        )

        Text(
            text = "우만시",
            modifier = Modifier.padding(top = 60.dp),
            textAlign = TextAlign.Center,
            style = getTextStyle(textStyle = RememberTextStyle.HEAD_1),
        )

        Button(
            onClick = { navController.navigate(RememberScreen.SignIn.name) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp, start = 16.dp, end = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 100.dp),
            border = BorderStroke(1.dp, fontPointColor)
        ) {
            Text(
                text = "회원가입",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(fontPointColor),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Preview
@Composable
fun TestDefaultPreview() {
    SplashScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: RememberScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            } else {
                // Todo 뒤로 가는 화면이 없는 경우 아이콘 추가
//                Icon(
//                    painter = painterResource(id = R.drawable.baseline_expand_more_24),
//                    contentDescription = "Image",
//                    tint = Color.Unspecified
//                )
            }
        }
    )
}