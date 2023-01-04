package com.cn.notesappkotlin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.notesample.models.UserRequest
import com.cheezycode.notesample.models.UserResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    val userResponseLiveData:LiveData<NetworkResult<UserResponse>>
    get() = repository.userResponseLiveData

     fun registerUser(userRequest: UserRequest){
        viewModelScope.launch { repository.registerUser(userRequest) }
    }

     fun loginUser(userRequest: UserRequest){
         viewModelScope.launch { repository.loginUser(userRequest) }
    }
}