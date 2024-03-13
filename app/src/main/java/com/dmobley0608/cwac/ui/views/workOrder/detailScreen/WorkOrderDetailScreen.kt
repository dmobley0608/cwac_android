package com.dmobley0608.cwac.ui.views.workOrder.detailScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.data.model.Priority
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.ui.theme.DisposeButton
import com.dmobley0608.cwac.ui.theme.PrimarySubmitButton
import com.dmobley0608.cwac.ui.theme.RejectButton
import com.dmobley0608.cwac.ui.utils.formatTimestamp
import com.dmobley0608.cwac.ui.views.LoadingScreen

import com.dmobley0608.cwac.ui.views.workOrder.addEditDialog.AddEditWorkOrderDialog


@Composable
fun WorkOrderDetailScreen(
    onMarkComplete: () -> Unit,
    onSendOrderBack: () -> Unit,
    workOrderId: String,
    workOrderDetailViewModel: WorkOrderDetailViewModel = viewModel {
        WorkOrderDetailViewModel(workOrderId)
    }
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val workOrder by workOrderDetailViewModel.workOrder.observeAsState(WorkOrder())
    val messages by workOrderDetailViewModel.messages.observeAsState(emptyList())
    val message = remember { mutableStateOf("") }
    val showEditDialog = remember { mutableStateOf(false) }
    val priority = when (workOrder.priority) {
        1 -> Priority.Low
        2 -> Priority.Moderate
        else -> Priority.High
    }
    Scaffold {
        Spacer(modifier = Modifier.padding(it))
        if(workOrderDetailViewModel.isLoading) {
            LoadingScreen()
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(priority.color),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${workOrder.title}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text("Date: ${formatTimestamp(workOrder.date!!)}", fontSize = 16.sp)
                    IconButton(onClick = { showEditDialog.value = !showEditDialog.value }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit work order")
                    }
                }
                Text("Submitted By: ${workOrder.createdBy}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Assigned To: ${workOrder.assignedTo?.firstname} ${workOrder.assignedTo?.lastName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Request: ${workOrder.description}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                if(!workOrder.complete!!){
                    Button(
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = PrimarySubmitButton,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        onClick = {
                            workOrderDetailViewModel.editWorkOrder(
                                order = workOrder.copy(
                                    complete = true,
                                    completedBy= workOrderDetailViewModel.currentUser.value
                                ))
                            onMarkComplete() })
                    {
                        Text("Mark As Complete")
                    }
                }else{
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            colors = ButtonColors(
                                containerColor = RejectButton,
                                contentColor = Color.White,
                                disabledContentColor = Color.Gray,
                                disabledContainerColor = Color.Gray
                            ),
                            onClick = {
                            workOrderDetailViewModel.editWorkOrder(order = workOrder.copy(complete = false))
                            onSendOrderBack()
                        }) {
                            Text("Send Back")
                        }
                        Button(
                            colors = ButtonColors(
                                containerColor = DisposeButton,
                                contentColor = Color.White,
                                disabledContentColor = Color.Gray,
                                disabledContainerColor = Color.Gray
                            ),
                            onClick = {
                            workOrderDetailViewModel.editWorkOrder(order = workOrder.copy(safeDeleted = true))
                            onSendOrderBack()
                        }) {
                            Text("Dispose")
                        }
                    }

                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                Text(text = "Notes:", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                // Display the chat messages
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) {
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
                        placeholder = {Text("Type Message")},
                        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                workOrderDetailViewModel.sendMessage(message.value.trim())
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
                                workOrderDetailViewModel.sendMessage(message.value.trim())
                                message.value = ""
                                keyboardController?.hide()
                            }

                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            }
            if(showEditDialog.value) {
                AddEditWorkOrderDialog(
                    workOrderDetailViewModel = workOrderDetailViewModel,
                    showDialog = showEditDialog,
                    navToWorkOrderDetail = { /*TODO*/ },
                    workOrder = workOrder
                )
            }
        }

    }

}



