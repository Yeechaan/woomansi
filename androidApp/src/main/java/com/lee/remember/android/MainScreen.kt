package com.lee.remember.android

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lee.remember.android.ui.friend.FriendAddScreen
import com.lee.remember.android.ui.friend.FriendEditScreen
import com.lee.remember.android.ui.friend.FriendGroupScreen
import com.lee.remember.android.ui.friend.FriendProfileScreen
import com.lee.remember.android.ui.friend.FriendScreen
import com.lee.remember.android.ui.home.HistoryScreen
import com.lee.remember.android.ui.home.MeetingScreen
import com.lee.remember.android.ui.intro.IntroScreen
import com.lee.remember.android.ui.intro.LoginScreen
import com.lee.remember.android.ui.intro.OnBoardingScreen
import com.lee.remember.android.ui.intro.SelectContractScreen
import com.lee.remember.android.ui.intro.SignUpScreen
import com.lee.remember.android.ui.intro.SplashScreen
import com.lee.remember.android.ui.intro.TermsScreen
import com.lee.remember.android.ui.intro.UserNameScreen
import com.lee.remember.android.ui.memory.MemoryAddScreen
import com.lee.remember.android.ui.memory.MemoryEditScreen
import com.lee.remember.android.ui.memory.MemoryFriendScreen
import com.lee.remember.android.ui.memory.MemoryScreen
import com.lee.remember.android.ui.my.MyScreen
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

enum class RememberScreen(@StringRes val title: Int) {
    Splash(title = R.string.splash),
    OnBoarding(title = R.string.onboard),
    Intro(title = R.string.intro),
    Terms(title = R.string.terms),
    SignUp(title = R.string.sign_up),
    UserName(title = R.string.user_name),
    Login(title = R.string.login),
    SelectContact(title = R.string.select_contact),

    History(title = R.string.history),
    Friend(title = R.string.friend),
    Memory(title = R.string.memory),

    FriendProfile(title = R.string.select_contact),
    FriendAdd(title = R.string.friend_edit),
    FriendEdit(title = R.string.friend_edit),
    FriendGroup(title = R.string.friend_group),

    MemoryFriend(title = R.string.memory_friend),
    MemoryAdd(title = R.string.memory_add),
    MemoryEdit(title = R.string.memory_edit),

    Meeting(title = R.string.memory),
    My(title = R.string.my)
}

val mainScreens = listOf(RememberScreen.History.name, RememberScreen.Memory.name, RememberScreen.Friend.name)

var selectedFriendGroup: String? = null
var bottomPadding: Dp = 0.dp

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = backStackEntry?.destination?.route ?: RememberScreen.Splash.name

    val bottomBarState = rememberSaveable {
        (mutableStateOf(mainScreens.contains(currentScreen)))
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (mainScreens.contains(currentScreen)) {
                RememberTopAppBarMain(navHostController = navController)
            }
        },
        bottomBar = {
            if (mainScreens.contains(currentScreen)) {
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
                                navController.navigate(mainScreen) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
//                                    navController.graph.findStartDestination().id {
//                                        popUpTo(it) { saveState = true }
//                                    }
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
//                .systemBarsPadding().statusBarsPadding().navigationBarsPadding()
        ) {
            composable(route = RememberScreen.Splash.name) {
                SplashScreen(navController)
            }
            composable(route = RememberScreen.OnBoarding.name) {
                OnBoardingScreen(navController)
            }
            composable(route = RememberScreen.Intro.name) {
                IntroScreen(navController)
            }
            composable(route = RememberScreen.Terms.name) {
                TermsScreen(navController)
            }
            composable(route = RememberScreen.SignUp.name) {
                SignUpScreen(navController, snackbarHostState)
            }
            composable(route = RememberScreen.UserName.name) {
                UserNameScreen(navController)
            }

            composable(route = RememberScreen.Login.name) {
                LoginScreen(navController, snackbarHostState)
            }
            composable(route = RememberScreen.SelectContact.name) {
                SelectContractScreen(navController)
            }
            composable(route = RememberScreen.History.name) {
                HistoryScreen(navController)
            }
            composable(route = RememberScreen.Friend.name) {
                FriendScreen(navController)
            }
            composable(route = RememberScreen.Memory.name) {
                MemoryScreen(navController)
            }
            composable(
                route = "${RememberScreen.FriendProfile.name}/{friendId}",
                arguments = listOf(navArgument("friendId") { type = NavType.StringType })
            ) {
                val friendId = it.arguments?.getString("friendId")
                FriendProfileScreen(navHostController = navController, friendId)
            }
            composable(
                route = "${RememberScreen.MemoryFriend.name}/{friendId}",
                arguments = listOf(navArgument("friendId") { type = NavType.StringType })
            ) {
                val friendId = it.arguments?.getString("friendId")
                MemoryFriendScreen(navHostController = navController, friendId)
            }
            composable(
                route = "${RememberScreen.FriendAdd.name}/{name}/{number}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType; nullable = true },
                    navArgument("number") { type = NavType.StringType; nullable = true })
            ) {
                val name = it.arguments?.getString("name", "")
                val number = it.arguments?.getString("number", "")
                FriendAddScreen(navHostController = navController, name, number)
            }
            composable(
                route = "${RememberScreen.FriendEdit.name}/{friendId}",
                arguments = listOf(navArgument("friendId") { type = NavType.StringType })
            ) {
                val friendId = it.arguments?.getString("friendId")
                FriendEditScreen(navHostController = navController, friendId)
            }
            composable(route = RememberScreen.FriendGroup.name) {
                FriendGroupScreen(navHostController = navController)
            }

            composable(
                route = "${RememberScreen.MemoryAdd.name}/{friendId}",
                arguments = listOf(navArgument("friendId") { type = NavType.StringType })
            ) {
                val friendId = it.arguments?.getString("friendId")
                MemoryAddScreen(navHostController = navController, snackbarHostState, friendId)
            }
            composable(
                route = "${RememberScreen.MemoryEdit.name}/{memoryId}",
                arguments = listOf(navArgument("memoryId") { type = NavType.StringType })
            ) {
                val friendId = it.arguments?.getString("memoryId")
                MemoryEditScreen(navHostController = navController, snackbarHostState, friendId)
            }

            composable(route = RememberScreen.Meeting.name) {
                MeetingScreen(navController)
            }
            composable(route = RememberScreen.My.name) {
                MyScreen(navController)
            }
        }

        bottomPadding = innerPadding.calculateBottomPadding()
    }
}


@Preview
@Composable
fun TestDefaultPreview() {
    SplashScreen(rememberNavController())
}

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
                Icon(
                    painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    currentScreen: RememberScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
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