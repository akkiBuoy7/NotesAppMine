package com.cn.notesappkotlin.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cheezycode.notesample.models.NoteRequest
import com.cheezycode.notesample.models.NoteResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.databinding.FragmentNoteBinding
import com.cn.notesappkotlin.utils.Constants.TAG
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding : FragmentNoteBinding? = null
    private val binding get() =  _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note:NoteResponse? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNoteBinding.inflate(inflater,container,false)

        initializeData()
        bindHandlers()
        bindObservers()

        return binding.root
    }


    private fun initializeData(){
        val jsonNote = arguments?.getString("note")
        if (jsonNote!=null){
            note = Gson().fromJson(jsonNote,NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }else{
            binding.addEditText.text = "Add Note"
        }

    }

    private fun bindHandlers(){

        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNotes(it._id) }
        }
        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(title,description)
            if (note!=null){

                Log.d(TAG, "bindHandlers: "+note!!._id)
                noteViewModel.updateNotes(note!!._id,noteRequest)
            }else{
                noteViewModel.createNotes(noteRequest)
            }
        }

    }

    private fun bindObservers(){
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {
                }
                is NetworkResult.Loading -> {
                }
            }

        })

    }
}