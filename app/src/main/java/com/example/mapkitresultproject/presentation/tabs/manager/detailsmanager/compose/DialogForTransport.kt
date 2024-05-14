package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.DialogEvent
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.MembersEvent

@Composable
fun DialogForTransport(
    modifier: Modifier = Modifier,
    openDialog: MutableState<DialogEvent>,
    data: TransportDialogData,
    onEvent: (MembersEvent) -> Unit
) {
    AlertDialog(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.7f),
        onDismissRequest = { openDialog.value = DialogEvent.HideTransportDialog },
        title = { Text(text = getTitle(openDialog.value as DialogEvent.OpenTransportDialog)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                InputTransportInfo(
                    placeholder = "Название*",
                    label = "",
                    text = data.name,
                    invalid = data.invalid,
                    keyboardTypeIsNumber = false
                )
                Spacer(modifier = Modifier.height(10.dp))
                InputTransportInfo(
                    placeholder = "Грузоп-сть*",
                    label = "Грузоп-сть, кг",
                    text = data.volume,
                    invalid = data.invalid
                )
                Spacer(modifier = Modifier.height(10.dp))
                InputTransportInfo(placeholder = "Высота",label = "Высота, м", text = data.height)
                Spacer(modifier = Modifier.height(10.dp))
                InputTransportInfo(placeholder = "Ширина",label = "Ширина, м", text = data.width)
                Spacer(modifier = Modifier.height(10.dp))
                InputTransportInfo(placeholder = "Длина",label = "Длина, м", text = data.length)
                Spacer(modifier = Modifier.height(10.dp))
                InputTransportInfo(placeholder = "Масса транспорта",label = "Масса, кг", text = data.weight)
            }
        },
        confirmButton = {
            Button(onClick = {
                confirmEvent(data = data, openDialog = openDialog) {
                    when ((openDialog.value as DialogEvent.OpenTransportDialog).transport == null ) {
                        true -> onEvent(MembersEvent.AddTransportItem(data.mapInTransport()))
                        false -> onEvent(MembersEvent.UpdateTransportItem(data.mapInTransport()))
                    }
                }
            }) {
                Text(text= getTextConfirmButton(openDialog.value as DialogEvent.OpenTransportDialog))
            }

        },
        dismissButton = {
            if((openDialog.value as DialogEvent.OpenTransportDialog).transport != null){
                Button(onClick = {
                    onEvent(MembersEvent.DeleteTransportItem(data.mapInTransport()))
                    openDialog.value = DialogEvent.HideTransportDialog
                }) {
                    Text(text= "Удалить")
                }
            }else null

        }
    )
}

@Composable
private fun InputTransportInfo(
    placeholder: String,
    text: MutableState<String>,
    label:String,
    invalid: MutableState<Boolean>? = null,
    keyboardTypeIsNumber: Boolean = true
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
        label = {Text(text=label)},
        singleLine = true,
        isError = invalid?.value ?: false,
        supportingText = { invalid?.let { if (it.value) Text("Пустое поле") else null } },
    )
}

private fun getTitle(openDialog: DialogEvent.OpenTransportDialog): String {
    return when (openDialog.transport == null) {
        true -> "Добавить транспорт"
        false -> "Изменить транспорт"
    }
}

private fun getTextConfirmButton(openDialog: DialogEvent.OpenTransportDialog): String {
    return when (openDialog.transport == null) {
        true -> "Добавить"
        false -> "Изменить"
    }
}

private fun confirmEvent(
    data: TransportDialogData,
    openDialog: MutableState<DialogEvent>,
    clickSaveTransport: () -> Unit
) {
    data.invalid.value = false
    if (data.name.value.isNotEmpty() && data.volume.value.isNotEmpty()) {
        clickSaveTransport()
        openDialog.value = DialogEvent.HideTransportDialog
    } else data.invalid.value = true
}
