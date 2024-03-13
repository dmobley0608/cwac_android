package com.dmobley0608.cwac.ui.views.workOrder.workOrderList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.ui.views.LoadingScreen
import com.dmobley0608.cwac.ui.views.workOrder.WorkOrderViewModel
import com.dmobley0608.cwac.ui.views.workOrder.addEditDialog.AddEditWorkOrderDialog
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WorkOrderListScreen(
    workOrderViewModel: WorkOrderViewModel = viewModel(),
    onNavToWorkOrder: (String) -> Unit
) {
    val workOrders by workOrderViewModel.workOrders.observeAsState(emptyList())
    var workOrderList = workOrders.filter { !it.complete!! }
    val showDialog = remember { mutableStateOf(false) }
    val pager = rememberPagerState(pageCount = { 2 })
    val coroutine = rememberCoroutineScope()
    when (pager.currentPage) {
        0 -> workOrderList = workOrders.filter { !it.complete!! }
        1 -> workOrderList = workOrders.filter { it.complete!! && !it.safeDeleted!! }
    }
    Scaffold(
        floatingActionButton = {
            // Button to create a new room
            FloatingActionButton(
                onClick = {
                    showDialog.value = true
                },

                ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Work Order")
            }
        }
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Text("Work Orders", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (workOrderViewModel.isLoading) {
                LoadingScreen()
            } else {
                if(workOrderViewModel.currentUser.value?.role == "ADMIN") {
                    HorizontalPager(state = pager) {
                        TabRow(selectedTabIndex = pager.currentPage) {
                            Tab(selected = pager.currentPage == 0, onClick = {
                                coroutine.launch {
                                    pager.scrollToPage(0)
                                }
                            }) {
                                Text(text = "Pending")
                            }
                            Tab(selected = pager.currentPage == 1, onClick = {
                                coroutine.launch {
                                    pager.scrollToPage(1)
                                }
                            }) {
                                Text(text = "Complete")
                            }
                        }
                    }
                }


                // Display a list of chat rooms
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    if(workOrderViewModel.currentUser.value?.role == "ADMIN"){
                        items(workOrderList) {
                            WorkOrderCard(order = it) { onNavToWorkOrder(it.id!!) }
                        }
                    }else{
                        items(workOrderList.filter { it.assignedTo == workOrderViewModel.currentUser.value }) {
                            WorkOrderCard(order = it) { onNavToWorkOrder(it.id!!) }
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            if (showDialog.value) {
                val order = WorkOrder()
                AddEditWorkOrderDialog(
                    showDialog = showDialog,
                    navToWorkOrderDetail = {},
                    workOrder = order
                )

            }
        }

    }


}


