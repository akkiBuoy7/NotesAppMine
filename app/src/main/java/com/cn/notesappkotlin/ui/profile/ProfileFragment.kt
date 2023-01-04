package com.cn.notesappkotlin.ui.profile

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.cn.notesappkotlin.R
import com.cn.notesappkotlin.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var imageUri : Uri
    private val contractCamera = registerForActivityResult(ActivityResultContracts.TakePicture()){
        binding.ivProfile.setImageURI(null)
        binding.ivProfile.setImageURI(imageUri)
    }

    private val contractGallery = registerForActivityResult(ActivityResultContracts.GetContent()){
        binding.ivProfile.setImageURI(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageUri = createImageUri()!!
        binding.btnCamera.setOnClickListener {
            contractCamera.launch(imageUri)
        }
        binding.btnGallery.setOnClickListener {
            contractGallery.launch("image/*")
        }

    }

    private fun createImageUri(): Uri?{
        val image = File(requireActivity().applicationContext.filesDir,"camera_pic")
        return FileProvider.getUriForFile(requireActivity().applicationContext,
            "com.cn.notesappkotlin.fileProvider",image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}