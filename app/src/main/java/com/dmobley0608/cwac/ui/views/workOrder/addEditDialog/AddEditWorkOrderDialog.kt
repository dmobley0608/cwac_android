package com.dmobley0608.cwac.ui.views.workOrder.addEditDialog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.data.model.Priority
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.ui.views.workOrder.WorkOrderViewModel
import com.dmobley0608.cwac.ui.views.workOrder.detailScreen.WorkOrderDetailViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddEditWorkOrderDialog(
    workOrderDetailViewModel: WorkOrderDetailViewModel = viewModel {
        WorkOrderDetailViewModel("")
    },
    workOrderViewModel:WorkOrderViewModel = viewModel(),
    showDialog: MutableState<Boolean>,
    navToWorkOrderDetail: () -> Unit,
    workOrder: WorkOrder
) {
    var title = remember { mutableStateOf(workOrder.title) }
    var description = remember { mutableStateOf(workOrder.description) }
    var selectPriorityText by remember { mutableStateOf( "Select Priority") }
    var selectEmployeeText by remember { mutableStateOf(workOrder.assignedTo?.firstname ?:"Select Employee") }
    var selectEmployeeValue by remember { mutableStateOf(workOrder.assignedTo) }
    var expandedPriority by remember { mutableStateOf(false) }
    var expandedEmployee by remember { mutableStateOf(false) }

    selectPriorityText = when(workOrder.priority){
        1 -> {Priority.Low.name!!}
        2-> Priority.Moderate.name!!
        3 -> Priority.High.name!!
        else -> "Select Priority"
    }
    Dialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .wrapContentSize()
                .padding(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                value = title.value!!,
                onValueChange = { title.value = it },
                label = { Text("Title") }
            )
            Text(text = ("Priority Level"))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))

            ) {

                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expandedPriority = !expandedPriority }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "select priority"
                        )
                        Text(text = selectPriorityText)
                    }

                }

                DropdownMenu(
                    expanded = expandedPriority,
                    onDismissRequest = { expandedPriority = false }) {
                    DropdownMenuItem(text = { Text("${Priority.Low.name}") }, onClick = {
                        selectPriorityText = "${Priority.Low.name}"
                        expandedPriority = false
                        workOrder.priority = Priority.Low.level
                    })
                    DropdownMenuItem(text = { Text("${Priority.Moderate.name}") }, onClick = {
                        workOrder.priority = Priority.Moderate.level
                        expandedPriority = false
                        selectPriorityText = "${Priority.Moderate.name}"
                    })
                    DropdownMenuItem(text = { Text("${Priority.High.name}") }, onClick = {
                        workOrder.priority = Priority.High.level
                        expandedPriority = false
                        selectPriorityText = "${Priority.High.name}"
                    })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Assigned To")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))


            ) {

                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expandedEmployee = !expandedEmployee }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "select employee"
                        )
                        Text(selectEmployeeText)
                    }

                }

                DropdownMenu(
                    expanded = expandedEmployee,
                    onDismissRequest = { expandedEmployee = false }) {
                    workOrderDetailViewModel.users.forEach {
                        user->
                        DropdownMenuItem(text = { Text(user.firstname) }, onClick = {
                            expandedEmployee = false
                            selectEmployeeText = user.firstname
                            selectEmployeeValue = user
                        })
                    }

//                    DropdownMenuItem(text = { Text("Kiser Roberts") }, onClick = {
//                        expandedEmployee = false
//                        selectEmployeeText = "Kiser Roberts"
//                    })
//                    DropdownMenuItem(text = { Text("Peyton Black") }, onClick = {
//                        expandedEmployee = false
//                        selectEmployeeText = "Peyton Black"
//                    })
//                    DropdownMenuItem(text = { Text("Dwight Mobley") }, onClick = {
//                        expandedEmployee = false
//                        selectEmployeeText = "Dwight Mobley"
//                    })
//                    DropdownMenuItem(text = { Text("Khip Miller") }, onClick = {
//                        expandedEmployee = false
//                        selectEmployeeText = "Khip Miller"
//                    })
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(150.dp),
                value = description.value!!,
                onValueChange = { description.value = it },
                label = { Text("Description") },

                )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    showDialog.value = false
                    val order =WorkOrder(
                        title = title.value,
                        description = description.value,
                        priority = workOrder.priority,
                        assignedTo = selectEmployeeValue,
                        createdBy = workOrderDetailViewModel.currentUser.value?.firstname
                    )
                    if (workOrderDetailViewModel.workOrder.value?.id?.isNotEmpty() == true) {
                        val updatedOrder = order.copy(id=workOrderDetailViewModel.workOrder.value?.id)
                        workOrderDetailViewModel.editWorkOrder(updatedOrder)
                    } else {
                            workOrderViewModel.createWorkOrder(order)
                            navToWorkOrderDetail()
                    }

                    showDialog.value = false
                }) {
                    Text(text = "Submit")
                }
            }
        }

    }
}