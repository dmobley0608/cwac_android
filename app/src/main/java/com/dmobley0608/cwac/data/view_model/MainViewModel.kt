package com.dmobley0608.cwac.data.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.repos.WorkOrderRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.dmobley0608.cwac.ui.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    val TAG = "MAIN VIEW MODEL"
    //Loading Status
    var isLoading by mutableStateOf(false)

    //Current User
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> get() = _currentUser

    private val _currentScreen:MutableState<Screen> = mutableStateOf(Screen.LoginScreen)
    val currentScreen:MutableState<Screen>
        get()=_currentScreen

    fun setCurrentScreen(screen:Screen){
        _currentScreen.value = screen
    }
    //Repos
    private val workOrderRepo: WorkOrderRepo
    private val userRepo: UserRepo

    init {
        Log.d(TAG, "INITIALIZING WORK ORDERS")
        workOrderRepo = WorkOrderRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        loadCurrentUser()
    }

    // Load Current User
    fun loadCurrentUser() {
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

    fun resetUser(){
        _currentUser.value = null
    }
}