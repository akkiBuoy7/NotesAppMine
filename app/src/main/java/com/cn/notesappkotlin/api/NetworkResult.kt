package com.cn.notesappkotlin.api

sealed class NetworkResult<T>(val data:T? = null,val message:String?=null) {
    class Success<T>(data:T) : NetworkResult<T>(data) // success state will always contain data
    class Error<T>(message:String?,data:T? = null) : NetworkResult<T>(data,message) // error state may contain data and must contain message
    class Loading<T>(): NetworkResult<T>() // loading state has no data or message
}