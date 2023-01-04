package com.cn.notesappkotlin.utils

import com.cn.notesappkotlin.api.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

open class APIUtils {
    suspend fun <T> safeApiCall(
    apiCall:suspend ()->T): NetworkResult<T>
    {
        return withContext(Dispatchers.IO){
            try {
                NetworkResult.Success(apiCall.invoke())
            }catch (throwable:Throwable){
                when(throwable){
                    is IOException -> NetworkResult.Error("IO Exception")
                    is HttpException -> {
                        val errorObject = JSONObject(throwable.response()!!.errorBody()!!.charStream().readText())
                        NetworkResult.Error(errorObject.getString("message"))
                    }else -> NetworkResult.Error("Something went wrong",null)
                }
            }
        }
    }
}