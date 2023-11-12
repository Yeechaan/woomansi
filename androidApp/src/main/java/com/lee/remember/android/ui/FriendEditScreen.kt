package com.lee.remember.android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendEditScreen(navHostController: NavHostController) {

    Column(
//        Modifier.fillMaxSize()
    ) {
        var name by remember { mutableStateOf("Harry") }
        var group by remember { mutableStateOf("favorite") }
        var number by remember { mutableStateOf("010-1111-1111") }
        var dateTitle by remember { mutableStateOf("생일") }
        var date by remember { mutableStateOf("2023/11/10") }

        val state = rememberDatePickerState()
        var selectedDate = state.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: "yyyy/mm/dd"

        androidx.compose.material3.TopAppBar(
            title = { Text("친구 기록", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
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
            actions = {
                TextButton(onClick = {
                    friendProfiles.add(FriendProfile(name, number, grouped = group, birthDate = selectedDate))

                    navHostController.navigateUp()
                }) {
                    Text(text = "완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B))
                }
            }
        )

        Image(painter = painterResource(id = R.drawable.ic_camera), contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(lightColor)
                .clickable {

                }
        )

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

                TextField(
                    value = items[selectedIndex], onValueChange = { group = it }, readOnly = true,
                    label = {
                        Text("그룹", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                    },
                    textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_expand_more_24),
                                contentDescription = "More"
                            )
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.Red
                        )
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

            TextField(
                value = number, onValueChange = { number = it },
                label = {
                    Text("연락처", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                },
                textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                var expanded by remember { mutableStateOf(false) }
                val items = listOf("생일", "기념일", "기일")
                var selectedIndex by remember { mutableStateOf(0) }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    Row {
                        Text(
                            items[selectedIndex],
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(color = Color(0xFF1D1B20)),
                            modifier = Modifier.clickable(onClick = { expanded = true })
                        )
                        Icon(painter = painterResource(id = R.drawable.baseline_expand_more_24), contentDescription = "")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Red
                            )
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

                val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        return utcTimeMillis <= System.currentTimeMillis()
                    }
                })

                Box {
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

                    TextField(
                        value = selectedDate, onValueChange = { selectedDate = it }, readOnly = true,
                        label = {
                            Text("이벤트", style = getTextStyle(textStyle = RememberTextStyle.BODY_4))
                        },
                        textStyle = getTextStyle(textStyle = RememberTextStyle.BODY_2),
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { openDialog.value = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calander),
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    )
                }
            }

            Text(
                text = "기록",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)
            )

            Button(
                onClick = {
                    friendProfiles.add(FriendProfile(name, number, grouped = group, birthDate = selectedDate))
                    navHostController.navigate(RememberScreen.HistoryAdd.name)
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Image(painter = painterResource(id = R.drawable.ic_plus), contentDescription = "")
                Text(
                    text = "기록추가",
                    style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(Color.Black),
                    modifier = Modifier.padding(start = 8.dp)
                )
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