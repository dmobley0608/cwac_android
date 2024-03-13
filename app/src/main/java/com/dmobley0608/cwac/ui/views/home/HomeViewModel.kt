package com.dmobley0608.cwac.ui.views.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Priority
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.model.WorkOrder
import com.dmobley0608.cwac.data.repos.ShowRepo
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.repos.WorkOrderRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.dmobley0608.cwac.ui.utils.formatShowDate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {
    val TAG = "HOME VIEW MODEL"

    //Current User
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> get() = _currentUser

    //Loading Status
    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean> get() = _isLoading

    // Work Orders
    private val _workOrders = MutableLiveData<List<WorkOrder>>(emptyList())
    val workOrders: LiveData<List<WorkOrder>>
        get() = _workOrders

    // Shows
    private val _shows = MutableLiveData<List<Show>>(emptyList())
    val shows: LiveData<List<Show>>
        get() = _shows

    //Repos
    private val workOrderRepo: WorkOrderRepo
    private val userRepo: UserRepo
    private val showRepo: ShowRepo

    init {
        Log.d(TAG, "INITIALIZING WORK ORDERS")
        workOrderRepo = WorkOrderRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        showRepo = ShowRepo(FirestoreInjection.instance())
        getPriorityWorkOrders()
        getUpcomingShows()
    }

    fun getPriorityWorkOrders() {
        _isLoading.value = true
        viewModelScope.launch {
            workOrderRepo.getWorkOrders().collect {
                _workOrders.value = it.filter {
                    order -> order.priority == Priority.High.level  &&
                        !order.complete!!
                }
                _isLoading.value = false
            }
        }
    }

    fun getUpcomingShows() {
        _isLoading.value = true
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        viewModelScope.launch {
            showRepo.getShows().collect {

                _shows.value = it.filter{
                    val endDate = LocalDate.parse(formatShowDate(it.endDate!!), formatter)
                    endDate >= LocalDate.now()
                }
                _isLoading.value = false
            }
        }
    }

}