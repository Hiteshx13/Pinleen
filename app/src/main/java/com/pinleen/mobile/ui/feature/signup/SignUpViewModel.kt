package com.pinleen.mobile.ui.feature.signup

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()
    val responseRegister = MutableLiveData<ResponseStartRegistration>()
    val responseRegisterEmail = MutableLiveData<ResponseStartRegistration>()

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

    fun registerEmailPassword(param: RequestRegisterEmail) {
        viewModelScope.launch {
            responseRegister.value = repository.registerEmailPassword(param)
        }
    }
}

