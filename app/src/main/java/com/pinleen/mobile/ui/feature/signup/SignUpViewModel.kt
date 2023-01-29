package com.pinleen.mobile.ui.feature.signup

import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Headers

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()
    val responseRegister = MutableLiveData<Response<ResponseStartRegistration>>()
    val responseRegisterEmail = MutableLiveData<Response<ResponseStartRegistration>>()

    init {
        register()
    }

    fun validateEmailPassword(email: String, password: String,listener: ValidEmailPasswordListener){

        val regexUppercase = Regex("[A-Z]")
        val regexLowercase = Regex("[a-z]")
        val regexNumbers = Regex("[0-9]")

        listener.isValid(  EMAIL_ADDRESS.matcher(email).matches(),
            regexUppercase.containsMatchIn(password),
            regexLowercase.containsMatchIn(password),
            regexNumbers.containsMatchIn(password),
            password.length in 6..30)
    }

    private fun register() {
        viewModelScope.launch {
            responseRegister.value = repository.register()
        }
    }

    fun registerEmailPassword(param: RequestRegisterEmail,mapAuth:Map<String,String>) {
        val callback=object: Callback<RequestRegisterEmail>{
            override fun onResponse(
                call: Call<RequestRegisterEmail>,
                response: Response<RequestRegisterEmail>
            ) {
                val headerList = response.headers()
                Log.d("headerList","$headerList")
            }

            override fun onFailure(call: Call<RequestRegisterEmail>, t: Throwable) {
                Log.d("","")
            }
        }
        viewModelScope.launch {
            responseRegisterEmail.value=repository.registerEmailPassword(param,mapAuth)
        }
    }
}

