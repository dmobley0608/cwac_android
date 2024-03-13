package com.dmobley0608.cwac.ui.views.workOrder

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.data.repos.WorkOrderRepo
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class WorkOrderViewModel : ViewModel() {
    val TAG = "WORK ORDER VIEW MODEL"

    //Loading Status
    var isLoading by mutableStateOf(false)

    // Work Orders
    private val _workOrders = MutableLiveData<List<WorkOrder>>()
    val workOrders: LiveData<List<WorkOrder>>
        get() = _workOrders

    //Work Order Id
    private val _workOrderId = MutableLiveData<String>()
    val workOrderId: LiveData<String>
        get() = _workOrderId

    fun setWorkOrders(value: List<WorkOrder>) {
        _workOrders.value = value
    }

    //Current User
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> get() = _currentUser

    //Repos
    private val workOrderRepo: WorkOrderRepo
    private val userRepo: UserRepo

    init {
        Log.d(TAG, "INITIALIZING WORK ORDERS")
        workOrderRepo = WorkOrderRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        loadCurrentUser()
        loadWorkOrders()
    }

    // Load Current User
    private fun loadCurrentUser() {
        isLoading = true
        viewModelScope.launch {
            when (val result = userRepo.getCurrentUser()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                    isLoading = false
                }

                is Result.Error -> {
                    Log.e(TAG, "ERROR FETCHING USER")
                    _currentUser.value = null
                    isLoading = false
                }
            }
        }
    }

    //Create Work Order
    fun createWorkOrder(order: WorkOrder) {
        isLoading = true
        viewModelScope.launch {
            when (val result = workOrderRepo.createWorkOrder(workOrder = order)) {
                is Result.Success -> _workOrderId.value = result.data
                else -> Log.e(TAG, "ERROR SETTING WORK ORDER ID")
            }
            isLoading = false
        }
    }

    //Fetch work orders
    fun loadWorkOrders() {
        isLoading = true
        viewModelScope.launch {
            workOrderRepo.getWorkOrders().collect { it ->
                _workOrders.value = it
            }

            isLoading = false
        }

    }
}