package com.dmobley0608.cwac.ui.views.workOrder.workOrderList

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmobley0608.cwac.data.model.Priority
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.ui.utils.formatTimestamp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkOrderCard(order: WorkOrder, navToWorkOrderDetail:()->Unit){
    var priority: Priority? = null
    if(order.priority == 1){
        priority = Priority.Low
    }else if(order.priority == 2){
        priority = Priority.Moderate
    }else{
        priority = Priority.High
    }

    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
            .clickable {
                navToWorkOrderDetail()
            }
    ) {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = priority.color)
        ) {
            Text("${priority.name}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly) {
            Text("${order.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Assigned To:\n\t${order.assignedTo?.firstname}",fontWeight = FontWeight.Bold)
            Text("Date:${formatTimestamp(order.date!!)}", fontSize = 10.sp)
        }

    }

}