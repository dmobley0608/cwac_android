package com.dmobley0608.cwac.ui.views.show.showDetailScreen


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow

import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.ui.theme.NavBarBackground
import com.dmobley0608.cwac.ui.theme.PrimaryBackground

import com.dmobley0608.cwac.ui.theme.SecondaryBackground
import com.dmobley0608.cwac.ui.utils.formatShowDate

import com.dmobley0608.cwac.ui.views.LoadingScreen
import com.dmobley0608.cwac.ui.views.show.AddEditShowDialog
import com.dmobley0608.cwac.ui.views.workOrder.detailScreen.MessageCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowDetailScreen(
    showKey: String,
    showViewModel: ShowDetailViewModel = viewModel {
        ShowDetailViewModel(showKey)
    }
) {
    val show by remember { mutableStateOf(showViewModel.show) }
    val messages by showViewModel.messages.observeAsState(emptyList())
    val message = remember { mutableStateOf("") }

    val showEditDialog = remember { mutableStateOf(false) }
    val showAddEquipmentDialog = remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutine = rememberCoroutineScope()

    if (showViewModel.isLoading) {
        LoadingScreen()
    } else {
        Scaffold {
            Column(modifier = Modifier
                .padding(it)
                .background(
                    brush = Brush.linearGradient(
                        start = Offset(1f, .5f),
                        colors = listOf(PrimaryBackground, SecondaryBackground)
                    )
                )) {
                HorizontalPager(state = pagerState) {
                    TabRow(

                        selectedTabIndex = pagerState.currentPage,
                        containerColor = NavBarBackground,
                        indicator = { tabPositions ->
                            SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]).padding(4.dp),
                                height = 2.dp,
                                color = Color.White
                            )
                        },
                    ) {
                        Tab(
                            modifier = Modifier.height(50.dp),
                            selected = pagerState.currentPage == 0,
                            onClick = {
                                coroutine.launch {
                                    pagerState.scrollToPage(0)
                                }
                            }) {
                            Text(text = "Details", color=Color.White)
                        }
                        Tab(
                            modifier = Modifier.height(50.dp),
                            selected = pagerState.currentPage == 1,
                            onClick = {
                                coroutine.launch {
                                    pagerState.scrollToPage(1)
                                }
                            }) {
                            Text(text = "Equipment", color=Color.White)
                        }
                        Tab(
                            modifier = Modifier.height(50.dp),
                            selected = pagerState.currentPage == 2,
                            onClick = {
                                coroutine.launch {
                                    pagerState.scrollToPage(2)
                                }
                            }) {
                            Text(text = "Notes", color=Color.White)
                        }
                    }

                }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 25.dp, start = 4.dp, end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    when (pagerState.currentPage) {
                        0 -> DetailSection(
                            show = show.value ?: Show(),
                            showEditDialog = showEditDialog,
                            showViewModel = showViewModel
                        )

                        1 -> EquipmentRequestedSection(
                            showAddEquipmentDialog = showAddEquipmentDialog,
                            show = show.value!!,
                            showViewModel = showViewModel,
                            coroutine = coroutine,
                            pager = pagerState
                        )

                        2 -> ChatSection(
                            messages = messages,
                            message = message,
                            showViewModel = showViewModel
                        )
                    }


                }

            }
            if (showEditDialog.value) {
                AddEditShowDialog(showAddShowDialog = showEditDialog, show = show.value!!) {
                }
            }

            if (showAddEquipmentDialog.value) {
                Dialog(onDismissRequest = { showAddEquipmentDialog.value = false }) {
                    var updatedShow = show.value?.copy()
                    val equipment = show.value?.equipmentRequested
                    var key by remember { mutableStateOf("") }
                    var value by remember { mutableStateOf("") }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = key,
                                onValueChange = { key = it },
                                label = { Text(text = "Item") })
                            OutlinedTextField(
                                value = value,
                                onValueChange = { value = it },
                                label = { Text(text = "Quantity") })
                            Button(onClick = {
                                equipment?.put(key, value)
                                updatedShow = updatedShow?.copy(equipmentRequested = equipment)
                                showViewModel.editShow(updatedShow!!)
                                showAddEquipmentDialog.value = false
                                coroutine.launch {
                                    pagerState.scrollToPage(1)
                                }

                            }) {
                                Text(text = "ADD")
                            }
                        }

                    }
                }


            }
        }
    }
}

@Composable
fun DetailSection(
    show: Show,
    showEditDialog: MutableState<Boolean>,
    showViewModel: ShowDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
           ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "${show.title}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            if (showViewModel.currentUser.value?.role == "ADMIN") {
                IconButton(onClick = { showEditDialog.value = !showEditDialog.value }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Details")
                }
            }
        }

        Text(
            "Arriving: ${formatShowDate(show.arrivalDate!!)}  ${show.arrivalTime}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Start Date: ${formatShowDate(show.startDate!!)}  ${show.startTime}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                "End Date: ${formatShowDate(show.endDate!!)} ${show.endTime}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Text(
            text = "Location: ${show.locationRequested}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "Ticketed Event: ${if (show.isChargedEvent!!) "Yes" else "No"}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "Show Manager: ${show.showManager}",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EquipmentRequestedSection(
    showAddEquipmentDialog: MutableState<Boolean>,
    show: Show,
    showViewModel: ShowDetailViewModel,
    coroutine: CoroutineScope,
    pager: PagerState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Equipment Request", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        if (showViewModel.currentUser.value?.role == "ADMIN") {
            IconButton(onClick = { showAddEquipmentDialog.value = !showAddEquipmentDialog.value }) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "add equipment")
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.spacedBy(100.dp)
    )
    {
        Column(modifier = Modifier.width(100.dp), horizontalAlignment = Alignment.Start) {
            Text(text = "Item")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Quantity")
        }
    }
    HorizontalDivider()
    LazyColumn {
        items(show.equipmentRequested?.toList()?.sortedBy { it.first } ?: emptyList()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Column(modifier = Modifier.width(100.dp), horizontalAlignment = Alignment.Start) {
                    Text(text = it.first)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = it.second)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (showViewModel.currentUser.value?.role == "ADMIN") {
                        IconButton(onClick = {
                            showViewModel.deleteEquipment(it.first)
                            coroutine.launch {
                                pager.scrollToPage(1)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Item"
                            )
                        }
                    }
                }
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun ChatSection(
    messages: List<Message>,
    message: MutableState<String>,
    showViewModel: ShowDetailViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Notes:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

        // Display the chat messages
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages.reversed()) {
                MessageCard(message = it.copy())
            }
        }
        // Chat input field and send icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = message.value,
                onValueChange = { message.value = it },
                placeholder = { Text("Type Message") },
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        showViewModel.sendMessage(message.value.trim())
                        message.value = ""
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )

            IconButton(
                onClick = {
                    // Send the message when the icon is clicked
                    if (message.value.isNotEmpty()) {
                        showViewModel.sendMessage(message.value.trim())
                        message.value = ""
                        keyboardController?.hide()
                    }

                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }

}


