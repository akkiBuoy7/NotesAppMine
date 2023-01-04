package com.cn.notesappkotlin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cheezycode.notesample.models.UserRequest
import com.cheezycode.notesample.models.UserResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.api.UserApi
import com.cn.notesappkotlin.utils.Constants.TAG
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: UserApi){

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = api.signup(userRequest)
        handleResponse(response)
        Log.d(TAG, "registerUser: "+response.body().toString())
    }

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = api.signin(userRequest)
        handleResponse(response)
        Log.d(TAG, "loginUser: "+response.body().toString())
    }

    private fun handleResponse(response:Response<UserResponse>){
        if (response.isSuccessful && response.body()!=null){
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody()!=null){
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(error.getString("message")))
        }else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong!!!"))
        }
    }
}