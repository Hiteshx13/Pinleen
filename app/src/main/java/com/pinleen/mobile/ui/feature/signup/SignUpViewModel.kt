package com.pinleen.mobile.ui.feature.signup

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.request.RequestRegisterNameAndPhone
import com.pinleen.mobile.data.models.response.ResponsePIK
import com.pinleen.mobile.utils.Constants.MAX_LENGTH_PASSWORD
import com.pinleen.mobile.utils.Constants.MIN_LENGTH_PASSWORD
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val repository = SignUpRepository()
    val responseRegister = MutableLiveData<Response<ResponsePIK>>()
    val deleteAccount = MutableLiveData<Response<ResponsePIK>>()
    val responseRegisterEmail = MutableLiveData<Response<ResponsePIK>>()
    val responseNameAndPhone = MutableLiveData<Response<ResponsePIK>>()

    fun validateEmailPassword(
        email: String,
        password: String,
        listener: ValidEmailPasswordListener
    ) {

        val regexUppercase = Regex("[A-Z]")
        val regexLowercase = Regex("[a-z]")
        val regexNumbers = Regex("[0-9]")

        listener.isValid(
            EMAIL_ADDRESS.matcher(email).matches(),
            regexUppercase.containsMatchIn(password),
            regexLowercase.containsMatchIn(password),
            regexNumbers.containsMatchIn(password),
            password.length in MIN_LENGTH_PASSWORD..MAX_LENGTH_PASSWORD
        )
    }

    fun register() {
        viewModelScope.launch {
            responseRegister.value = repository.register()
        }
    }
    fun deleteAccount(email:String) {
        viewModelScope.launch {
            deleteAccount.value = repository.deleteAccount(email)
        }
    }

    fun registerEmailPassword(param: RequestRegisterEmail, mapAuth: Map<String, String>) {
        viewModelScope.launch {
            responseRegisterEmail.value = repository.registerEmailPassword(param, mapAuth)
        }
    }

    fun registerUserNameAndPhone(param: RequestRegisterNameAndPhone, mapAuth: Map<String, String>) {
        viewModelScope.launch {
            responseNameAndPhone.value = repository.registerUserNameAndPhone(param, mapAuth)
        }
    }
}

