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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.ui.fontColorBlack
import com.lee.remember.android.ui.lightColor
import com.lee.remember.android.utils.RememberCheckbox
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.android.viewmodel.FriendViewModel
import com.lee.remember.remote.request.FriendRequest
import kotlinx.coroutines.launch

@Composable
fun SelectContractScreen(
    navController: NavHostController,
    viewModel: FriendViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
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
    if (uiState.loading) {
        // Todo 로딩 처리
    }

    val contracts = mutableListOf<Contract>()
    contracts.addAll(getContracts())

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
                        modifier = Modifier.padding(top = 16.dp, bottom = 48.dp)
                    )
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
                .background(Color(0xFFF2BE2F)),
            onClick = {
                scope.launch {
                    try {
                        val selectedFriends = contracts.filter { it.isChecked }.map {
                            FriendRequest(it.name, it.number)
                        }

                        viewModel.addFriends(selectedFriends)
                    } catch (e: Exception) {
                        e.localizedMessage ?: "error"
                    }
                }
            }) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = "선택완료",
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