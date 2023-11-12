package com.lee.remember.android.ui

import android.annotation.SuppressLint
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.lee.remember.android.data.FriendProfile
import com.lee.remember.android.friendProfiles
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle

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
            Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .weight(weight = 1f, fill = false),
        ) {

            item {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_camera), contentDescription = "",
                        modifier = Modifier
                            .padding(top = 48.dp)
                    )
                    Text(
                        text = "내가 간직할 소중한 인연을\n선택해보세요.",
                        style = getTextStyle(textStyle = RememberTextStyle.HEAD_3).copy(textAlign = TextAlign.Center),
                        modifier = Modifier.padding(top = 16.dp, bottom = 48.dp)
                    )
                    Divider(thickness = 8.dp, color = Color(0xFFF7F7F7))
                }
            }

            items(contracts) { message ->
                Row() {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text(text = message.name)
                        Text(text = message.number)
                    }

                    val checkedState = remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = {
                            contracts.find { it.number == message.number }?.isChecked = it
                            checkedState.value = it
                        },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFFF2C678), uncheckedColor = Color(0xFFF2C678))
                    )
                }
            }
        }
        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8D393)),
            onClick = {
                contracts.filter { it.isChecked }.forEach {
                    friendProfiles.add(FriendProfile(it.name, it.number))
                }

                navController.navigate(RememberScreen.History.name)
            }) {
            Text(
                text = "선택완료",
                style = getTextStyle(textStyle = RememberTextStyle.BODY_2B).copy(Color(0xFF50432E))
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

        Log.d("###", "count: ${it.count}")

        while (it.moveToNext()) {
            val id = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
            val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            userContracts.add(Contract(id, name, number))
        }
    }

    cursor?.close()

    val aa = userContracts.distinctBy { it.id }
    Log.d("###", "${aa.size}")
    Log.d("###", "$aa")

    return aa
}

@Preview
@Composable
fun SelectContractScreenPreview() {
    SelectContractScreen(rememberNavController())
}