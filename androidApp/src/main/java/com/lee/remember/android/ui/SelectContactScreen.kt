package com.lee.remember.android.ui

import android.annotation.SuppressLint
import android.provider.ContactsContract
import android.util.Log
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
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.lee.remember.GreetingKtor
import com.lee.remember.android.Contract
import com.lee.remember.android.R
import com.lee.remember.android.RememberScreen
import com.lee.remember.android.accessToken
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import com.lee.remember.request.FriendMultiAddRequest
import com.lee.remember.request.UserInfoRequest
import kotlinx.coroutines.launch

val contracts = mutableListOf<Contract>()

@Composable
fun SelectContractScreen(navController: NavHostController) {
    contracts.addAll(getContracts())
//    val contracts = getContracts()

    ContactList(contracts, navController)
}

@Composable
fun ContactList(contracts: List<Contract>, navController: NavHostController) {
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
                        .fillMaxWidth().background(Color.White)
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

            items(contracts) { message ->
                Row(
                    Modifier
//                        .padding(start = 16.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
                        .background(Color.White)
                ) {
                    Column(
                        Modifier.weight(1f).background(Color.White)
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                            text = message.name,
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_1B).copy(
                                color = fontColorBlack,
                                textAlign = TextAlign.Center
                            ),
                        )
                        Text(
                            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                            text = message.number,
                            style = getTextStyle(textStyle = RememberTextStyle.BODY_4).copy(
                                color = Color(0x61000000),
                                textAlign = TextAlign.Center
                            ),
                        )
                    }

                    val checkedState = remember { mutableStateOf(false) }
                    Checkbox(
                        modifier = Modifier.padding(end = 16.dp),
                        checked = checkedState.value,
                        onCheckedChange = {
                            contracts.find { it.number == message.number }?.isChecked = it
                            checkedState.value = it
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2BE2F), uncheckedColor = Color.Black)
                    )
                }
            }
        }

        val scope = rememberCoroutineScope()
        var text by remember { mutableStateOf("") }

        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2BE2F)),
            onClick = {
                contracts.filter { it.isChecked }.forEach {
                    friendProfiles.add(FriendProfile(it.name, it.number))
                }

                scope.launch {
                    text = try {
                        val friends = friendProfiles.map {
                            FriendMultiAddRequest.Friend(it.name, it.phoneNumber, it.image)
                        }

                        // TODO
                        val aa = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1MSIsImlhdCI6MTcwMDY1MTAzMywiZXhwIjoxNzAxNTE1MDMzfQ.reVAg1JzThO-3QHvI6WIV4VzcTJo24JpDl_bYSf61U0"
                        val response = GreetingKtor().addFriendMulti(aa, FriendMultiAddRequest(friends))

                        if (response != null) {
                            navController.navigate(RememberScreen.History.name)

                            response.toString()
                        } else {
                            "에러"
                        }
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

//        Log.d("###", "count: ${it.count}")

        while (it.moveToNext()) {
            val id = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
            val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            userContracts.add(Contract(id, name, number))
        }
    }

    cursor?.close()

    val aa = userContracts.distinctBy { it.id }
//    Log.d("###", "${aa.size}")
//    Log.d("###", "$aa")

    return aa
}

@Preview
@Composable
fun SelectContractScreenPreview() {
    SelectContractScreen(rememberNavController())
}