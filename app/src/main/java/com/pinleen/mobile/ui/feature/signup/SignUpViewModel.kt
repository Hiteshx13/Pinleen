package com.pinleen.mobile.ui.feature.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import kotlinx.coroutines.launch
import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import com.pinleen.mobile.data.models.request.RequestRegisterEmail

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()
    val responseRegister=MutableLiveData<ResponseStartRegistration>()
    val responseRegisterEmail=MutableLiveData<ResponseStartRegistration>()
    init {
        register()
    }

    fun isValidData(email:String,password:String):Boolean{
      return EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun register() {
         viewModelScope.launch {
             responseRegister.value =  repository.register()
         }
    }
    fun registerEmailPassword(param: RequestRegisterEmail) {
         viewModelScope.launch {
             responseRegister.value =  repository.registerEmailPassword(param)
         }
    }
}

