package com.lee.remember.android

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.lee.remember.android.ui.intro.IntroScreen
import com.lee.remember.android.ui.intro.SplashScreen
import com.lee.remember.android.ui.intro.TermsScreen
import com.lee.remember.android.ui.intro.UserNameScreen
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

enum class RememberScreen(@StringRes val title: Int) {
    Splash(title = R.string.splash),
    Intro(title = R.string.intro),
    Terms(title = R.string.terms),
    SignIn(title = R.string.sign_in),
    UserName(title = R.string.user_name),

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
                            label = { Text(stringResource(id = mainScreen.title), style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(Color(0xFF49454F))) },
                            icon = {
                                val resourceId = when(mainScreen) {
                                    RememberScreen.History -> R.drawable.ic_friend_off
                                    RememberScreen.Feed -> R.drawable.ic_feed_off
                                    RememberScreen.Friend -> R.drawable.ic_contact_off
                                    else -> R.drawable.ic_dot
                                }

                                // Todo when item clicked
                                Icon(
                                    painterResource(id = resourceId),
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
            composable(route = RememberScreen.Intro.name) {
                IntroScreen(navController)
            }
            composable(route = RememberScreen.Terms.name) {
                TermsScreen(navController)
            }
            composable(route = RememberScreen.SignIn.name) {
                SignInScreen(navController)
            }
            composable(route = RememberScreen.UserName.name) {
                UserNameScreen(navController)
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


@Preview
@Composable
fun TestDefaultPreview() {
    SplashScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberTopAppBar(){
    TopAppBar(
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
            IconButton(onClick = {}) {
                Icon(painter = painterResource(id = R.drawable.ic_account), contentDescription = "")
            }
        }
    )
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