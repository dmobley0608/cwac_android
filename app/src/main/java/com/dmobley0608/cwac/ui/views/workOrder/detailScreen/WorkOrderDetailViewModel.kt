package com.dmobley0608.cwac.ui.views.workOrder.detailScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.repos.WorkOrderRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class WorkOrderDetailViewModel(key:String): ViewModel() {
    val TAG = "WORK ORDER DETAIL VIEW MODEL"

    //Loading Status
    var isLoading by mutableStateOf(false)

    // Work Order
    private val _workOrder = MutableLiveData<WorkOrder>()
    val workOrder: LiveData<WorkOrder>
        get() = _workOrder

    //Current User
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    //Current User
    private val _users:MutableState<List<User>> = mutableStateOf(emptyList())
    val users: List<User> get() = _users.value

    //Messages
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    //Repos
    private val workOrderRepo: WorkOrderRepo
    private val userRepo: UserRepo

    init {
        Log.d(TAG, "INITIALIZING WORK ORDERS")
        workOrderRepo = WorkOrderRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        loadCurrentUser()
        loadWorkOrder(key)
        loadAllUsers()
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
                    isLoading = false
                }
            }
        }
    }
    //load All Users
    fun loadAllUsers(){
        isLoading = true
        viewModelScope.launch {
           _users.value =  userRepo.getAllUsers()
            isLoading = false
        }
    }

    fun loadWorkOrder(key:String){
        isLoading = true
        viewModelScope.launch {
           when(val result = workOrderRepo.fetchWorkOrderByKey(key)){
               is Result.Success -> _workOrder.value = result.data
               else -> Log.e(TAG, "ERROR FETCHING WORK ORDER DETAILS")
           }
            loadMessages()
           isLoading = false
        }
    }
    //Edit Work Order
    fun editWorkOrder(order:WorkOrder){
        isLoading = true
        viewModelScope.launch{
            when(val result =workOrderRepo.editWorkOrder(order)) {
                is Result.Success -> _workOrder.value = result.data
                else->Log.e(TAG, "ERROR EDITING WORK ORDER")
            }
            isLoading = false
        }
    }



    //Fetch Work Order Messages
    fun loadMessages() {
        viewModelScope.launch {
            if (_workOrder.value != null) {
                workOrderRepo.getWorkOrderMessages(_workOrder.value!!.id.toString())
                    .collect { _messages.value = it }
            }
        }
    }

    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val message = Message(
                author = _currentUser.value!!.firstname,
                senderId = _currentUser.value!!.key,
                text = text
            )
            viewModelScope.launch {
                when (workOrderRepo.sendMessage(_workOrder.value!!.id!!, message)) {
                    is Result.Success -> Unit
                    is Result.Error -> Log.e(TAG, "ERROR SENDING MESSAGE")
                }
            }
        }
    }

}