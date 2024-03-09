package com.lee.remember.android.ui.friend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.lee.remember.android.R
import com.lee.remember.android.ui.selectedFriendGroup
import com.lee.remember.android.utils.RememberTextField
import com.lee.remember.android.utils.RememberTextStyle
import com.lee.remember.android.utils.getTextStyle
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendGroupScreen(navHostController: NavHostController) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var name by remember { mutableStateOf("") }

        val _groups = remember { MutableStateFlow(mutableListOf("Calls", "Missed", "Friends")) }
        val groups by remember { _groups }.collectAsState()
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(_groups.value[0]) }

        TopAppBar(
            modifier = Modifier.shadow(elevation = 1.dp),
            title = { Text("새 그룹 추가", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
            navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = stringResource(R.string.back_button))
                }
            },
            actions = {
                TextButton(onClick = {
                    selectedFriendGroup = selectedOption
                    navHostController.navigateUp()
                }) {
                    Text("완료", style = getTextStyle(textStyle = RememberTextStyle.BODY_2B))
                }
            }
        )

        TextField(
            value = name, onValueChange = { name = it },
            placeholder = { RememberTextField.placeHolder(text = "그룹 이름을 입력하세요.") },
            textStyle = RememberTextField.textStyle(),
            colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = fontColorPoint, unfocusedBorderColor = fontColorPoint),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    val newGroups = ArrayList(groups)
                    newGroups.add(name)
                    _groups.value = newGroups
                }) {
                    Image(painter = painterResource(id = R.drawable.ic_add), contentDescription = null)
                }
            },
            modifier = Modifier
                .padding(vertical = 28.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        )

        Divider(thickness = 8.dp, color = Color(0xffEFEEEC))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 12.dp),
            text = "내 그룹", style = getTextStyle(textStyle = RememberTextStyle.HEAD_5)
        )

        // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
        Column(Modifier.selectableGroup()) {
            groups.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text,
                        style = getTextStyle(textStyle = RememberTextStyle.BODY_1B),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    )
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null, // null recommended for accessibility with screenreaders,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFF2BE2F)),
                        modifier = Modifier.padding(end = 24.dp)
                    )
                }
            }
        }

    }
}

//@Preview
//@Composable
//fun PreviewFriendGroupScreen() {
//    FriendGroupScreen(rememberNavController())
//}