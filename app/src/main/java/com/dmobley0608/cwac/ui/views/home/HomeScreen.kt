package com.dmobley0608.cwac.ui.views.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.ui.theme.PrimaryBackground
import com.dmobley0608.cwac.ui.theme.SecondaryBackground
import com.dmobley0608.cwac.ui.views.LoadingScreen
import com.dmobley0608.cwac.ui.views.show.ShowCard
import com.dmobley0608.cwac.ui.views.workOrder.workOrderList.WorkOrderCard

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navToWorkOrderDetail: (String) -> Unit,
    navToShowDetail: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold() {
        Spacer(modifier = Modifier.padding(it))
        Column(
            modifier = Modifier
                .padding()
                .fillMaxSize()
                .verticalScroll(state = scrollState)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(PrimaryBackground, SecondaryBackground)
                    )
                )
        ) {
            if (viewModel.isLoading.value) {
                LoadingScreen()
            } else {
                Column(modifier = Modifier.padding(4.dp)) {
                    //A Arena Shows
                    Text(text = "A Arena Shows")
                    if (viewModel.shows.value?.filter { it.locationRequested == "A Arena" }
                            .isNullOrEmpty()) {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "No Up-coming Shows")
                            }

                        }
                    }
                    LazyRow {
                        items(viewModel.shows.value?.filter { it.locationRequested == "A Arena" }
                            ?: emptyList()) {
                            ShowCard(
                                show = it,
                                navToShowDetail = { navToShowDetail(it.key!!) })
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    //Activity Hall Reservations
                    Text(text = "Activity Hall Reservations")
                    if (viewModel.shows.value?.filter { it.locationRequested == "Activity Hall" }
                            .isNullOrEmpty()) {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "No Up-coming Shows")
                            }

                        }
                    }
                    LazyRow {
                        items(viewModel.shows.value?.filter { it.locationRequested == "Activity Hall" }
                            ?: emptyList()) {
                            ShowCard(
                                show = it,
                                navToShowDetail = { navToShowDetail(it.key!!) })
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    //Other
                    Text(text = "Other Areas")
                    if (viewModel.shows.value?.filter { it.locationRequested != "Activity Hall" && it.locationRequested != "A Arena" }
                            .isNullOrEmpty()) {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "No Up-coming Shows")
                            }

                        }
                    }
                    LazyRow {
                        items(viewModel.shows.value?.filter { it.locationRequested != "Activity Hall" && it.locationRequested != "A Arena" }
                            ?: emptyList()) {
                            ShowCard(
                                show = it,
                                navToShowDetail = { navToShowDetail(it.key!!) })
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                    //Work Orders
                    if (viewModel.workOrders.value?.size!! > 0) {
                        Text(text = "Priority Work Orders")
                        LazyRow {
                            items(viewModel.workOrders.value ?: emptyList()) {
                                WorkOrderCard(
                                    order = it,
                                    navToWorkOrderDetail = { navToWorkOrderDetail(it.id!!) })
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    }

                }

            }

            /*TODO*/
//            //News
//            Text(text = "News and Info")
//            if (viewModel.workOrders.value.isNullOrEmpty()) {
//                Card(modifier = Modifier.size(100.dp)) {
//                    Column(
//                        modifier = Modifier.padding(8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(text = "Nothing to see here")
//                    }
//
//                }
//            }
//            LazyRow {
//                items(viewModel.workOrders.value ?: emptyList()) {
//                    WorkOrderCard(
//                        order = it,
//                        navToWorkOrderDetail = { navToWorkOrderDetail(it.id!!) })
//                }
//            }
//            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
//
        }
    }

}
