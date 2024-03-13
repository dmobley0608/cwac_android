package com.dmobley0608.cwac.ui.views.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import com.dmobley0608.cwac.data.model.InventoryItem
import com.dmobley0608.cwac.ui.views.LoadingScreen

@Composable
fun InventoryScreen(
    inventoryViewModel: InventoryViewModel = viewModel()
) {
    val inventoryList by inventoryViewModel.inventoryList.observeAsState(emptyList())
    Scaffold(modifier = Modifier.fillMaxSize()) {
        if (inventoryViewModel.isLoading) {
            LoadingScreen()
        } else {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                        Text("Barcode/Name", fontWeight = FontWeight.Bold)

                       Text(text = "Quantity", fontWeight = FontWeight.Bold)

                }
                HorizontalDivider()
                LazyColumn {
                    items(inventoryList) {
                        InventoryItemRow(item = it, viewModel = inventoryViewModel)
                    }
                }

            }

        }
    }
}

@Composable
fun InventoryItemRow(item: InventoryItem, viewModel: InventoryViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(item.barcode ?: "Unknown")
            Text(item.name ?: "Unknown")
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.width(25.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                IconButton(onClick = {
                    val updatedItem = item.copy(quantity = item.quantity?.minus(1))
                    viewModel.editInventoryItem(item.key!!,updatedItem)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Subtract"
                    )
                }
            }
            Column(modifier = Modifier.width(25.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("${item.quantity}")
            }
            Column(modifier = Modifier.width(25.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                IconButton(onClick = {
                    val updatedItem = item.copy(quantity = item.quantity?.plus(1))
                    viewModel.editInventoryItem(item.key!!,updatedItem)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
    HorizontalDivider()
}