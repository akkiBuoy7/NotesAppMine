package com.cn.notesappkotlin.safe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cheezycode.notesample.models.NoteRequest
import com.cn.notesappkotlin.repository.NotesRepository
import com.cn.notesappkotlin.repository.NotesSafeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class NoteSafeViewModel @Inject constructor(private val repository: NotesSafeRepository): ViewModel() {

    val noteLiveData get() = repository.noteLiveData
    val statusLiveData get() = repository.statusLiveData



    fun getNotes(){
        viewModelScope.launch { repository.getNotes() }
    }


    fun createNotes(noteRequest: NoteRequest){
        viewModelScope.launch { repository.createNote(noteRequest) }
    }

    fun deleteNotes(noteId:String){
        viewModelScope.launch { repository.deleteNote(noteId) }
    }
    fun updateNotes(noteId:String ,noteRequest: NoteRequest){
        viewModelScope.launch { repository.updateNote(noteId,noteRequest) }
    }
}