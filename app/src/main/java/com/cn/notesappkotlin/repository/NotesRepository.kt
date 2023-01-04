package com.cn.notesappkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cheezycode.notesample.models.NoteRequest
import com.cheezycode.notesample.models.NoteResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.api.NoteApi
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteApi:NoteApi) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
     val noteLiveData :LiveData<NetworkResult<List<NoteResponse>>>
    get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
     val statusLiveData:LiveData<NetworkResult<String>>
    get() = _statusLiveData


    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNotes()
        handleGetResponse(response)
    }

    private fun handleGetResponse(response: Response<List<NoteResponse>>){
        if (response.isSuccessful && response.body()!=null){
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody()!=null){
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(error.getString("message")))
        }else{
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong!!!"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleStatusResponse(response,"Note Created")

    }

    suspend fun updateNote(noteId:String,noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(noteId,noteRequest)
        handleStatusResponse(response,"Note Updated")

    }

    suspend fun deleteNote(noteId:String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleStatusResponse(response,"Note Deleted")
    }

    private fun handleStatusResponse(response: Response<NoteResponse>,message:String){
        if (response.isSuccessful && response.body()!=null){
            _statusLiveData.postValue(NetworkResult.Success(message))
        }else if (response.errorBody()!=null){
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _statusLiveData.postValue(NetworkResult.Error(error.getString("message")))
        }else{
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong!!!"))
        }

    }

}