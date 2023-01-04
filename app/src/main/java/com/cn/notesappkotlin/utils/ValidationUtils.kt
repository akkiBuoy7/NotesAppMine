package com.cn.notesappkotlin.utils

import android.text.TextUtils
import android.util.Patterns
import com.cheezycode.notesample.models.UserRequest
import java.util.regex.Pattern

object  ValidationUtils {

    fun validation(userRequest: UserRequest,isLogin:Boolean):Pair<Boolean,String>{
        var result = Pair(true,"")
        if ((!isLogin && TextUtils.isEmpty(userRequest.username))|| TextUtils.isEmpty(userRequest.email.toString()) ||TextUtils.isEmpty(userRequest.password.toString())){
            result = Pair(false,"Please fill all the credentials")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(userRequest.email).matches()){
            result = Pair(false,"Please enter valid email address")
        }else if (userRequest.password.length<=5){
            result = Pair(false,"Password length should be greater than 5")

        }
        return result
    }
}