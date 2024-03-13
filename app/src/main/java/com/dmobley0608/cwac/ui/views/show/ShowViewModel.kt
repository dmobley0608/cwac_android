package com.dmobley0608.cwac.ui.views.show

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.Show
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.repos.ShowRepo
import com.dmobley0608.cwac.data.repos.UserRepo
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ShowViewModel : ViewModel() {
    val TAG = "WORK ORDER VIEW MODEL"

    //Loading Status
    var isLoading by mutableStateOf(false)

    //Shows
    private val _shows = MutableLiveData<List<Show>>()
    val shows: LiveData<List<Show>>
        get() = _shows

    //Current User
    private val _currentUser = MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> get() = _currentUser

    //Repos
    private val showRepo: ShowRepo
    private val userRepo: UserRepo

    init {
        Log.d(TAG, "INITIALIZING SHOWS")
        showRepo = ShowRepo(FirestoreInjection.instance())
        userRepo = UserRepo(FirebaseAuth.getInstance(), FirestoreInjection.instance())
        loadCurrentUser()
        loadShows()

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

    //Create Show
    fun createShow(show: Show) {
        isLoading = true
        viewModelScope.launch {
            showRepo.createShow(show = show)
            isLoading = false
        }
    }

    //Fetch Shows
    fun loadShows() {
        isLoading = true
        viewModelScope.launch {
            showRepo.getShows().collect { show ->
                _shows.value = show
            }
        }
    }
}