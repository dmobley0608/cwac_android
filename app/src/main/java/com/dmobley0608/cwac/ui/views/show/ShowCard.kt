package com.dmobley0608.cwac.ui.views.show

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.ui.utils.formatShowDate


@Composable
fun ShowCard(show: Show, navToShowDetail: () -> Unit) {

    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(8.dp)
            .clickable {
                navToShowDetail()
            },
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    formatShowDate(show.arrivalDate!!),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("${show.title}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Location:\n\t${show.locationRequested}", fontWeight = FontWeight.Bold)
                Text("Show Manager:${show.showManager}", fontSize = 10.sp)
            }
        }


    }
}