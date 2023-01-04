package com.cn.notesappkotlin.ui.note

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cheezycode.notesample.models.NoteResponse
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.safe.NoteSafeViewModel
import com.cn.notesappkotlin.R
import com.cn.notesappkotlin.databinding.FragmentMainBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private val noteSafeViewModel by viewModels<NoteSafeViewModel>()

    private lateinit var noteAdapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater,container,false)

        noteAdapter = NoteAdapter(::noteClicked)

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindObservers()
        //bindSafeObservers()
        noteViewModel.getNotes()
        //noteSafeViewModel.getNotes()
        binding.noteList.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = noteAdapter
    }


    private fun bindObservers(){
        noteViewModel.noteLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    noteAdapter.submitList(it.data)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }

        })

    }

    private fun bindSafeObservers(){
        noteSafeViewModel.noteLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    noteAdapter.submitList(it.data!!.body())
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(),it.message.toString(),Toast.LENGTH_SHORT)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }

        })

    }

    private fun noteClicked(note:NoteResponse){
        val bundle = Bundle()
        bundle.putString("note",Gson().toJson(note))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment,bundle)

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}