package com.pinleen.mobile.ui.feature.login

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import com.pinleen.mobile.ui.feature.signup.ValidEmailPasswordListener
import com.pinleen.mobile.utils.Constants.MAX_LENGTH_PASSWORD
import com.pinleen.mobile.utils.Constants.MIN_LENGTH_PASSWORD
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()
    val responseRegister = MutableLiveData<Response<ResponseStartRegistration>>()

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


//    fun registerUserNameAndPhone(param: RequestRegisterNameAndPhone, mapAuth: Map<String, String>) {
//        viewModelScope.launch {
//            responseNameAndPhone.value = repository.registerUserNameAndPhone(param, mapAuth)
//        }
//    }
}

