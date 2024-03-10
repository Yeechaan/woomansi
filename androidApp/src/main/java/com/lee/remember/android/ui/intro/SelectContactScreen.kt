package com.lee.remember.android.ui.intro

import android.annotation.SuppressLint
import android.provider.ContactsContract
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.R
import com.lee.remember.android.ui.RememberScreen
import com.lee.remember.android.ui.friend.fontColorBlack
import com.lee.remember.android.ui.friend.lightColor
import com.lee.remember.android.ui.common.RememberCheckbox
import com.lee.remember.android.ui.common.RememberTextStyle
import com.lee.remember.android.ui.common.getTextStyle
import com.lee.remember.android.viewmodel.friend.FriendViewModel
import com.lee.remember.model.Contract
import com.lee.remember.remote.request.FriendRequest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SelectContractScreen(
    navController: NavHostController,
    viewModel: FriendViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.success) {
        viewModel.resetUiState()
        navController.navigate(RememberScreen.History.name) {
            popUpTo(RememberScreen.Intro.name) {
                inclusive = true
            }
        }
    }

    var openDialog by remember { mutableStateOf(false) }
    if (openDialog) {
        DunbarNumberDialog(
            onDismissRequest = {
                openDialog = false
            },
            onConfirm = {
                openDialog = false
            }
        )
    }

    val contracts = mutableListOf<Contract>()
    contracts.addAll(getContracts())

    var selectedContractCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            Modifier.weight(weight = 1f, fill = false),
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_select_contract), contentDescription = "",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "내가 간직할 소중한 인연을\n선택해보세요.",
                        style = getTextStyle(textStyle = RememberTextStyle.HEAD_4).copy(textAlign = TextAlign.Center),
                        modifier = Modifier.padding(top = 20.dp)
                    )
                    Text(
                        text = "많은 사람들을 선택하기보다\n내 인생에 값진 인연들만 관리해 보세요",
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(
                            textAlign = TextAlign.Center,
                            color = Color(0x58000000)
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    TextButton(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(bottom = 24.dp),
                        onClick = { openDialog = true }) {
                        Text(
                            text = "던바의 수",
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_2).copy(Color(0xFFD99A14)),
                            modifier = Modifier.drawBehind {
                                val strokeWidthPx = 1.dp.toPx()
                                val verticalOffset = size.height - 1.sp.toPx()
                                drawLine(
                                    color = Color(0xFFD99A14),
                                    strokeWidth = strokeWidthPx,
                                    start = Offset(0f, verticalOffset),
                                    end = Offset(size.width, verticalOffset)
                                )
                            }
                        )
                    }
                }
            }

            item {
                Divider(thickness = 8.dp, color = lightColor)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(Color.White)
                )
            }

            items(contracts) { contract ->
                Row(
                    Modifier
//                        .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                        .background(Color.White)
                ) {
                    Column(
                        Modifier
                            .weight(1f)
                            .background(Color.White)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                            text = contract.name,
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(
                                color = fontColorBlack,
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            text = contract.number,
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(
                                color = Color(0x61000000),
                                textAlign = TextAlign.Center
                            ),
                        )
                    }

                    val checkedState = remember { mutableStateOf(contract.isChecked) }
                    Checkbox(
                        modifier = Modifier.padding(end = 16.dp),
                        checked = checkedState.value,
                        onCheckedChange = {
                            contract.isChecked = it
                            checkedState.value = it

                            if (it) selectedContractCount++ else selectedContractCount--
                        },
                        colors = RememberCheckbox()
                    )
                }
            }
        }

        val scope = rememberCoroutineScope()

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD99A14)),
            onClick = {
                scope.launch {
                    try {
                        val selectedFriends = contracts.filter { it.isChecked }.map {
                            FriendRequest(it.name, it.number, events = listOf())
                        }

                        viewModel.addFriends(selectedFriends)
                    } catch (e: Exception) {
                        e.localizedMessage ?: "error"
                    }
                }
            }) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = "인연 맺어가기 ${selectedContractCount}명",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color.Black)
            )
        }
    }
}

@SuppressLint("Range")
@Composable
fun getContracts(): List<Contract> {
    val context = LocalContext.current
    val userContracts = mutableListOf<Contract>()

    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
    )

    cursor?.let {
        while (it.moveToNext()) {
            val id = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
            val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            userContracts.add(Contract(id, name, number))
        }
    }
    cursor?.close()

    return userContracts.distinctBy { it.id }
}

@Preview
@Composable
fun SelectContractScreenPreview() {
    SelectContractScreen(rememberNavController())
}