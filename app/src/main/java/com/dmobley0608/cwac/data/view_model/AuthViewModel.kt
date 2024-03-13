package com.dmobley0608.cwac.data.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmobley0608.cwac.data.model.Result
import com.dmobley0608.cwac.data.model.User
import com.dmobley0608.cwac.data.utils.FirestoreInjection
import com.dmobley0608.cwac.data.repos.UserRepo
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    val TAG = "AUTH VIEW MODEL"
    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean> get() = _isLoading

    private val _error = mutableStateOf("")
    val error: MutableState<String> get() = _error

    private val userRepo: UserRepo
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult
    var userIsAuthenticated = mutableStateOf(false)

    init {
        userRepo = UserRepo(
            FirebaseAuth.getInstance(),
            FirestoreInjection.instance()
        )
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "${FirebaseAuth.getInstance().currentUser?.email}")
            userIsAuthenticated.value = true
        }
    }

    fun signUp(email: String, password: String, firstName: String, lastName: String, role: String) {
        _isLoading.value = true
        viewModelScope.launch {
            userRepo.signUp(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                role = role
            ).let { result ->
                if (result.equals(Result.Success(User()))) {
                    signIn(email, password)
                    userIsAuthenticated.value = true
                    _authResult.value = Result.Success(true)
                }else{
                    _error.value=result.toString()
                }
            }


            _isLoading.value = false
        }
    }


    fun signIn(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _authResult.value = userRepo.signIn(email, password)
            if (_authResult.value == Result.Success(data = true)) {
                userIsAuthenticated.value = true
            }
            _isLoading.value = false
        }

    }

    fun signOut() {
        _isLoading.value = true
        FirebaseAuth.getInstance().signOut()
        userIsAuthenticated.value = false
        _isLoading.value = false

    }
}