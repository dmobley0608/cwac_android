package com.dmobley0608.cwac.ui.views.show.showDetailScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Message
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.repos.ShowRepo
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ShowDetailViewModel(key:String): ViewModel() {
    val TAG = "SHOW DETAIL VIEW MODEL"

    //Loading Status
    var isLoading by mutableStateOf(false)

    // Work Order
    private val _show = MutableLiveData<Show>()
    val show: LiveData<Show>
        get() = _show

    //Current User
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    //Messages
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    //Repos
    private val showRepo: ShowRepo
    private val userRepo: UserRepo

    init {
        Log.d(TAG, "INITIALIZING SHOW DETAILS")
        showRepo =ShowRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        loadCurrentUser()
        loadShow(key)
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

    fun loadShow(key:String){
        isLoading = true
        viewModelScope.launch {
            when(val result = showRepo.fetchShowByKey(key)){
                is Result.Success -> _show.value = result.data
                else -> Log.e(TAG, "ERROR FETCHING SHOW DETAILS")
            }
            Log.d(TAG, "${show.value}")
            loadMessages()
            isLoading = false
        }
    }
    //Edit Work Order
    fun editShow(show: Show){
        isLoading = true
        viewModelScope.launch{
            when(val result =showRepo.editShow(show)) {
                is Result.Success -> _show.value = result.data
                else-> Log.e(TAG, "ERROR EDITING SHOW")
            }
            isLoading = false
        }
    }

    //Delete Equipment
    fun deleteEquipment(key:String){
        isLoading = true
        viewModelScope.launch {
            val show = show.value?.copy()
            show?.equipmentRequested?.remove(key)
            showRepo.editShow(show!!)
            isLoading = false
        }
        isLoading = false


    }



    //Fetch Work Order Messages
    fun loadMessages() {
        Log.d(TAG, "FETCHING MESSAGES FOR SHOW")
        viewModelScope.launch {
            if (_show.value != null) {
                showRepo.getShowMessages(_show.value!!.key.toString())
                    .collect { _messages.value = it }
            }
            Log.d(TAG, "${_messages.value}")
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
                when (showRepo.sendMessage(_show.value!!.key!!, message)) {
                    is Result.Success -> Unit
                    is Result.Error -> Log.e(TAG, "ERROR SENDING MESSAGE")
                }
            }
        }
    }
}