package com.cn.notesappkotlin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cheezycode.notesample.models.UserRequest
import com.cn.notesappkotlin.api.NetworkResult
import com.cn.notesappkotlin.R
import com.cn.notesappkotlin.databinding.FragmentRegisterBinding
import com.cn.notesappkotlin.utils.TokenManager
import com.cn.notesappkotlin.utils.ValidationUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding:FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager:TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (tokenManager.getToken()!=null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }


        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first){
                authViewModel.registerUser(getUserRequest())
            }else{
                binding.txtError.text = validationResult.second
            }
        }
        bindObservers()
    }



    private fun bindObservers(){
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false

            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }


    private fun validateUserInput():Pair<Boolean,String>{
        val userRequest = getUserRequest()
        return  ValidationUtils.validation(userRequest,false)
    }

    private fun getUserRequest():UserRequest{
        val emailAddress = binding.txtEmail.text.toString()  // akashTest@gmail.com
        val userName = binding.txtEmail.text.toString() // akashTest
        val password = binding.txtEmail.text.toString() // 12345678
        return UserRequest(emailAddress,password,userName)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}