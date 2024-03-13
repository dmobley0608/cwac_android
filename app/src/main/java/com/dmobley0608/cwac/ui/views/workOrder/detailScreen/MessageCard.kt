package com.dmobley0608.cwac.ui.views.workOrder.detailScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.ui.theme.PurpleGrey80
import com.dmobley0608.cwac.ui.utils.formatTimestamp


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageCard(message: Message) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(PurpleGrey80, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)

        ) {
            Text(
                text = message.text!!,
                color = Color.Black,
                style = TextStyle(fontSize = 16.sp),
                textAlign = TextAlign.Left
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalAlignment = Alignment.End){
            Text(
                text = message.author!!,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.End

            )
            Text(
                text = formatTimestamp(message.date!!), // Replace with actual timestamp logic
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            )
        }

    }
}