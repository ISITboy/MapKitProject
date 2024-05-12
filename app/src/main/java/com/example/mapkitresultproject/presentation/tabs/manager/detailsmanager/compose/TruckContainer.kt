package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.models.Transport

@Composable
fun TruckContainer(modifier: Modifier = Modifier) {
    var isVisible by remember { mutableStateOf(false) }
    val slideOffset by animateDpAsState(
        targetValue = if (isVisible) 150.dp else (0).dp,
        animationSpec = tween(durationMillis = 300))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(60.dp)
                .clickable { },
            painter = painterResource(id = R.drawable.ic_bus_80),
            contentDescription = null
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = Modifier
                .height(60.dp)
                .width(slideOffset)
        ){
            items(listOf(
                Transport(name ="Фура",volume =4000.0),
                Transport(name="Бус", volume = 1000.0),
                Transport(name="Машина", volume = 300.0),
            )) { transport ->
                TransportItem(transport = transport)
            }
        }
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable { isVisible = !isVisible },
            imageVector = if(isVisible)Icons.Default.KeyboardArrowLeft else Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }
}


@Composable
fun TransportItem(modifier: Modifier = Modifier, transport: Transport){
    Card(
        modifier = modifier.padding(horizontal = 3.dp),
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(width = 1.dp, Color.White)
    ){
        Column(
            modifier = Modifier.background(colorResource(id = R.color.card_view_background)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(transport.name, fontSize = 15.sp ,modifier= Modifier.padding(horizontal = 2.dp))
            Spacer(modifier = Modifier.height(5.dp))
            Text(transport.volume.toString(), fontSize = 15.sp,modifier= Modifier.padding(horizontal = 2.dp))
            Spacer(modifier = Modifier.height(5.dp))
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = "кг.", fontSize = 10.sp)
        }
    }

}
