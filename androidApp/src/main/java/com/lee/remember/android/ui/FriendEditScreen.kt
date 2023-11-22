package com.lee.remember.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendHistory
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.selectedFriendPhoneNumber
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendEditScreen(navHostController: NavHostController) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var name by remember { mutableStateOf("") }
        var group by remember { mutableStateOf("favorite") }
        var number by remember { mutableStateOf("") }
        var dateTitle by remember { mutableStateOf("생일") }
        var date by remember { mutableStateOf("2023/11/10") }

        val state = rememberDatePickerState()
        var selectedDate = state.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: "yyyy/mm/dd"

        androidx.compose.material3.TopAppBar(
            modifier = Modifier.shadow(elevation = 1.dp),
            title = { Text("친구 기록", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            },
            actions = {
                TextButton(onClick = {
                    friendProfiles.add(FriendProfile(name, number, grouped = group, birthDate = selectedDate))

                    navHostController.navigateUp()
                }) {
                    Text(text = "완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B))
                }
            }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(lightColor)
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera), contentDescription = "",
                modifier = Modifier.size(74.dp)
            )
        }

        Divider(thickness = 1.dp, color = Color.Black)

        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {

            TextField(
                value = name, onValueChange = { name = it },
                textStyle = getTextStyle(textStyle = RememberTextStyle.HEAD_5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart)
            ) {
                var expanded by remember { mutableStateOf(false) }
                val items = listOf("가족", "가장 친한", "친해지고 싶은")
                var selectedIndex by remember { mutableStateOf(0) }

                OutlinedTextField(
                    value = items[selectedIndex], onValueChange = { group = it }, readOnly = true,
                    label = {
                        Text("그룹", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                    },
                    textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            if (!expanded) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_expand_more_24),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_expand_less_24),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(text = {
                            Text(text = s)
                        }, onClick = {
                            selectedIndex = index
                            expanded = false
                        })
                    }
                }
            }

            OutlinedTextField(
                value = number, onValueChange = { number = it },
                label = {
                    Text("연락처", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                },
                textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { number = "" }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cancel),
                            contentDescription = "Clear"
                        )
                    }
                }
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 12.dp)) {
                var expanded by remember { mutableStateOf(false) }
                val items = listOf("생일", "기념일", "기일")
                var selectedIndex by remember { mutableStateOf(0) }

                TextButton(
                    onClick = { expanded = true },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight()
                        .padding()
                ) {
                    Text(
                        items[selectedIndex],
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(color = Color(0xFF1D1B20))
                    )
                    if (!expanded) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_expand_more_24),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_expand_less_24),
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                ) {
                    items.forEachIndexed { index, s ->
                        DropdownMenuItem(text = {
                            Text(text = s)
                        }, onClick = {
                            selectedIndex = index
                            expanded = false
                        })
                    }
                }


                val openDialog = remember { mutableStateOf(false) }
                if (openDialog.value) {
                    DatePickerDialog(
                        onDismissRequest = { openDialog.value = false },
                        confirmButton = {
                            TextButton(onClick = { openDialog.value = false }) {
                                Text("확인", style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(fontColorPoint))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { openDialog.value = false }
                            ) {
                                Text("취소", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                            }
                        }
                    ) { DatePicker(state = state) }
                }

                OutlinedTextField(
                    value = selectedDate, onValueChange = { selectedDate = it }, readOnly = true,
                    label = {
                        Text("이벤트", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                    },
                    textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = { openDialog.value = true }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calander),
                                contentDescription = "Clear"
                            )
                        }
                    }
                )
            }

            Text(
                text = "기록",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)
            )

            TextButton(
                onClick = {
                    friendProfiles.add(FriendProfile(name, number, grouped = group, birthDate = selectedDate))
                    navHostController.navigate(RememberScreen.HistoryAdd.name)
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Color(0xFFD8D8D8), shape = RoundedCornerShape(size = 16.dp)),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
                Text(
                    text = "기록추가",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(Color.Black),
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp)
                )
            }

            val friendProfile = friendProfiles.find { it.phoneNumber == selectedFriendPhoneNumber }
            val items = friendProfile?.history ?: listOf<FriendHistory>()

            LazyColumn(
                Modifier
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp)
            ) {
                items(items) { item ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        FeedItem(friendProfile?.name ?: "", item)
                    }
                }
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd")
    return formatter.format(Date(millis))
}


@Preview
@Composable
fun PreviewFriendEditScreen() {
    FriendEditScreen(rememberNavController())
}