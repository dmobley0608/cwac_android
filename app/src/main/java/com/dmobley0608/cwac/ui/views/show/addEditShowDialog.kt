package com.dmobley0608.cwac.ui.views.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.ui.utils.DateVisualTransformation

import com.dmobley0608.cwac.ui.views.show.showDetailScreen.ShowDetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditShowDialog(
    showAddShowDialog: MutableState<Boolean>,
    showViewModel: ShowViewModel = viewModel(),
    show: Show,

    handleOnSubmit: () -> Unit
) {

    val show = remember { mutableStateOf(show) }
    val showDetailViewModel: ShowDetailViewModel = viewModel {
        ShowDetailViewModel(show.value.key ?: "")
    }

    val arrivalDateState = remember { mutableStateOf("") }
    val startDateState = remember { mutableStateOf("") }
    val endDateState = remember { mutableStateOf("") }
    var expandedLocation by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = { showAddShowDialog.value = false }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(state=scrollState),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Select Arrival Date
            OutlinedTextField(
                value = show.value.arrivalDate!!,
                onValueChange = {
                    if (it.length <= 8) {
                        show.value = show.value.copy(arrivalDate = it)
                    }
                },
                singleLine = true,
                placeholder = { Text(text = "MM-DD-YYY") },
                label = { Text("Arrival Date") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = DateVisualTransformation()
            )
//
            //Arrival Time
            OutlinedTextField(
                value = show.value.arrivalTime!!,
                onValueChange = { show.value = show.value.copy(arrivalTime = it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text("Arrival Time") }
            )
            //Select Start Date
            OutlinedTextField(
                value = show.value.startDate!!,
                onValueChange = {
                    if (it.length <= 8) {
                        show.value = show.value.copy(startDate = it)
                    }
                },
                singleLine = true,
                placeholder = { Text(text = "MM-DD-YYY") },
                label = { Text("Start Date") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = DateVisualTransformation()
            )
            //Start Time
            OutlinedTextField(
                value = show.value.startTime!!,
                onValueChange = { show.value = show.value.copy(startTime = it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text("Start Time") }
            )
            //Select End Date
            OutlinedTextField(
                value = show.value.endDate!!,
                onValueChange = {
                    if (it.length <= 8) {
                        show.value = show.value.copy(endDate = it)
                    }
                },
                singleLine = true,
                placeholder = { Text(text = "MM-DD-YYY") },
                label = { Text("End Date") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = DateVisualTransformation()
            )
            //End Time
            OutlinedTextField(
                value = show.value.endTime!!,
                onValueChange = { show.value = show.value.copy(endTime = it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text("End Time") }
            )
            //Title
            OutlinedTextField(
                value = show.value.title!!,
                onValueChange = { show.value = show.value.copy(title = it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text("Show Title") }
            )
            //Show Manager
            OutlinedTextField(
                value = show.value.showManager!!,
                onValueChange = { show.value = show.value.copy(showManager = it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                label = { Text("Show Manager") }
            )
            //Ticketed Event
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ticketed Event")
                Switch(
                    checked = show.value.isChargedEvent!!,
                    onCheckedChange = { show.value = show.value.copy(isChargedEvent = true) })
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(5.dp))

            ) {

                IconButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expandedLocation = !expandedLocation }) {
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
                        Text(
                            text = "${
                                if (show.value.locationRequested?.isNotEmpty() == true) show.value.locationRequested
                                else "Select Location"
                            }"
                        )
                    }

                }

                DropdownMenu(
                    expanded = expandedLocation,
                    onDismissRequest = { expandedLocation = false }) {
                    DropdownMenuItem(text = { Text("Activity Hall") }, onClick = {
                        expandedLocation = false
                        show.value = show.value.copy(locationRequested = "Activity Hall")
                    })
                    DropdownMenuItem(text = { Text("A Arena") }, onClick = {
                        expandedLocation = false
                        show.value = show.value.copy(locationRequested = "A Arena")
                    })
                    DropdownMenuItem(text = { Text("C Arena") }, onClick = {
                        expandedLocation = false
                        show.value = show.value.copy(locationRequested = "C Arena")
                    })
                    DropdownMenuItem(text = { Text("D Arena") }, onClick = {
                        expandedLocation = false
                        show.value = show.value.copy(locationRequested = "D Arena")
                    })
                    DropdownMenuItem(text = { Text("A,C,D Arena") }, onClick = {
                        expandedLocation = false
                        show.value = show.value.copy(locationRequested = "A,C, D Arena")
                    })
                }
            }


            //Submit Button
            Button(onClick = {
                if (show.value.key?.isNotEmpty() == true) {
                    showDetailViewModel.editShow(show.value)
                } else {
                    showViewModel.createShow(show.value)
                }
                showAddShowDialog.value = false
                handleOnSubmit()
            }) {
                Text(text = "Submit")
            }
        }
    }
}


@Composable
fun DateTextField(label: String, onValueChange:()->Unit) {
var date by remember {mutableStateOf("")}
    OutlinedTextField(
        value = date,
        onValueChange = {
            if (it.length <= 8) {
               date = it
            }
        },
        singleLine = true,
        placeholder = { Text(text = "MM-DD-YYY") },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = DateVisualTransformation()
    )
}





