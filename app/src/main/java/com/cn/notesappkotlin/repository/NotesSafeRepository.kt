package com.cn.notesappkotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cheezycode.notesample.models.NoteRequest
import com.cheezycode.notesample.models.NoteResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.api.NoteApi
import com.cn.notesappkotlin.utils.APIUtils
import retrofit2.Response
import javax.inject.Inject

class NotesSafeRepository @Inject constructor(private val noteApi: NoteApi): APIUtils() {


    private val _notesLiveData = MutableLiveData<
            NetworkResult<Response<List<NoteResponse>>>>()
    val noteLiveData : LiveData<
            NetworkResult<Response<List<NoteResponse>>>>
        get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Response<NoteResponse>>>()
    val statusLiveData: LiveData<NetworkResult<Response<NoteResponse>>>
        get() = _statusLiveData


    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        val x = safeApiCall{noteApi.getNotes()}
        _notesLiveData.postValue(x)
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
         val y = safeApiCall{noteApi.createNote(noteRequest)}
        _statusLiveData.postValue(y)
    }

    suspend fun updateNote(noteId:String,noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
         val z = safeApiCall{noteApi.updateNote(noteId,noteRequest)}
        _statusLiveData.postValue(z)
    }

    suspend fun deleteNote(noteId:String){
        _statusLiveData.postValue(NetworkResult.Loading())
         val s = safeApiCall{noteApi.deleteNote(noteId)}
        _statusLiveData.postValue(s)
    }

}