package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.DialogEvent
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.MembersEvent

@Composable
fun DetailsManagerCompose(
    modifier: Modifier = Modifier,
    providers: List<Shipper>,
    consumers: List<Consignee>,
    event: (MembersEvent) -> Unit,
) {
    val openDialog = remember { mutableStateOf<DialogEvent>(DialogEvent.HideDialog) }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.shipper),
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(5.dp))
        ContainerMembers(members = providers,
            addMembersClick = {
                openDialog.value = DialogEvent.OpenShipperDialog
            },
            swipeToDelete = {
                event(MembersEvent.DeleteShipperItem(it as Shipper))
            })
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.consignees),
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(5.dp))
        ContainerMembers(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(horizontal = 5.dp),
            members = consumers,
            addMembersClick = {
                openDialog.value = DialogEvent.OpenConsigneeDialog
            },
            swipeToDelete = {
                event(MembersEvent.DeleteConsigneeItem(it as Consignee))
            }
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            TruckContainer()
            Button(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp,Color.White),
                onClick = { /*TODO*/ }
            ) {
                Text(text = "Маршрут")
            }
        }

        when (openDialog.value) {
            DialogEvent.HideDialog -> {}
            DialogEvent.OpenConsigneeDialog -> DialogForMembers(
                openDialog = openDialog,
                placeholder = "Адрес ГП"
            ){
                event(it)
            }
            DialogEvent.OpenShipperDialog -> DialogForMembers(
                openDialog = openDialog,
                placeholder ="Адрес ГО"
            ){
                event(it)
            }
        }

    }
}

@Composable
private fun <T> ContainerMembers(
    modifier: Modifier = Modifier,
    members: List<T>,
    addMembersClick: () -> Unit,
    swipeToDelete: (Any) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .padding(horizontal = 5.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(width = 5.dp, colorResource(id = R.color.card_border_color))
    ) {
        Box(modifier = Modifier.background(colorResource(id = R.color.card_view_background))) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                items(members) { member ->
                    SwipeBox(
                        onEdit = {

                        },
                        onDelete = {
                            if (member is Shipper) swipeToDelete(member)
                            if (member is Consignee) swipeToDelete(member)

                        }) {
                        if (member is Shipper) TextItemProvider(address = member.address)
                        if (member is Consignee) TextItemConsumer(consignee = member)
                    }


                }
            }
            Card(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 8.dp)
                    .alpha(0.6f),
                shape = RoundedCornerShape(15.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                border = BorderStroke(width = 1.dp, colorResource(id = R.color.black))
            ) {
                IconButton(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    onClick = addMembersClick
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun TextItemProvider(modifier: Modifier = Modifier, address: String) {
    val expanded = remember {
        mutableStateOf(true)
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .background(colorResource(id = R.color.card_view_item_background)),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BaseText(text = address, expanded = expanded)
        }
    }
}

@Composable
fun TextItemConsumer(modifier: Modifier = Modifier, consignee: Consignee) {
    val expanded = remember {
        mutableStateOf(true)
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .background(colorResource(id = R.color.card_view_item_background)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BaseText(consignee.address, expanded)
            BaseText(consignee.volume.toString(), expanded, false)
        }
    }
}

@Composable
fun BaseText(text: String, expanded: MutableState<Boolean>, flag: Boolean = true) {
    Text(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .clickable { expanded.value = !expanded.value },
        text = if (flag) text else "$text кг",
        color = Color.Black,
        style = MaterialTheme.typography.bodyLarge,
        overflow = TextOverflow.Ellipsis,
        maxLines = if (expanded.value) 1 else 10
    )
}

