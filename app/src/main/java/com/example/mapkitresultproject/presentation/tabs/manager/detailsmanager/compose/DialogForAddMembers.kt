package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.DialogEvent
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.MembersEvent

@Composable
fun DialogForMembers(
    modifier: Modifier = Modifier,
    openDialog: MutableState<DialogEvent>,
    placeholder: String,
    event: (MembersEvent) -> Unit
) {
    val texts = remember {
        mutableListOf<Pair<MutableState<String>, MutableState<Boolean>>>()
    }
    AlertDialog(
        onDismissRequest = { openDialog.value = DialogEvent.HideDialog;texts.clear() },
        title = { Text(text = "Добавить члена") },
        text = {
            if (openDialog.value == DialogEvent.OpenConsigneeDialog) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    texts.add(Pair(mutableStateOf(""), mutableStateOf(false)))
                    texts.add(Pair(mutableStateOf(""), mutableStateOf(false)))
                    InputMemberInfo(
                        placeholder = placeholder,
                        invalid = texts.first().second,
                        text = texts.first().first
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InputMemberInfo(
                        placeholder = "масса",
                        invalid = texts.last().second,
                        text = texts.last().first,
                        keyboardTypeIsNumber = true
                    )
                }
            } else {
                texts.add(Pair(mutableStateOf(""), mutableStateOf(false)))
                InputMemberInfo(
                    placeholder = placeholder,
                    invalid = texts.first().second,
                    text = texts.first().first
                )
            }

        },
        confirmButton = {
            Button({
                confirmEvent(
                    texts = texts,
                    openDialog = openDialog,
                    clickSaveShipper = { event(MembersEvent.AddShipperItem(Shipper(address = texts.first().first.value))) },
                    clickSaveConsignee = {
                        event(
                            MembersEvent.AddConsigneeItem(
                                Consignee(
                                    address = texts.first().first.value,
                                    volume = texts.last().first.value.toDouble()
                                )
                            )
                        )
                    }
                )

            }) {
                Text("OK", fontSize = 22.sp)
            }
        },
        dismissButton = {
            Button({ openDialog.value = DialogEvent.HideDialog; texts.clear() }) {
                Text("Назад", fontSize = 22.sp)
            }
        }
    )
}

@Composable
private fun InputMemberInfo(
    placeholder: String,
    text: MutableState<String>,
    invalid: MutableState<Boolean>,
    keyboardTypeIsNumber: Boolean = false
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = text.value,
        onValueChange = { text.value = it },
        placeholder = { Text(text = placeholder) },
        trailingIcon = {
            if (text.value.isNotBlank()) {
                IconButton(onClick = { text.value = "" }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (keyboardTypeIsNumber) KeyboardType.Number else KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        isError = invalid.value,
        supportingText = {
            if (invalid.value) Text("Пустое поле") else null
        },
    )
}

private fun confirmEvent(
    texts: MutableList<Pair<MutableState<String>, MutableState<Boolean>>>,
    openDialog: MutableState<DialogEvent>,
    clickSaveConsignee: () -> Unit,
    clickSaveShipper: () -> Unit
) {
    texts.forEach { it.second.value = false }
    if (texts.all { it.first.value.isNotEmpty() }) {
        when (texts.size) {
            1 -> clickSaveShipper()
            2 -> clickSaveConsignee()
        }
        texts.clear()
        openDialog.value = DialogEvent.HideDialog
    } else {
        texts.filter { it.first.value.isEmpty() }.forEach {
            it.second.value = true
        }
    }
}
