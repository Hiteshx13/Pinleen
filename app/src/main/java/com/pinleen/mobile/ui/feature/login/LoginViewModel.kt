package com.pinleen.mobile.ui.feature.login

import android.util.Patterns.EMAIL_ADDRESS
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.request.RequestLogin
import com.pinleen.mobile.data.models.response.ResponsePIK
import com.pinleen.mobile.ui.feature.signup.ValidEmailPasswordListener
import com.pinleen.mobile.utils.Constants.MAX_LENGTH_PASSWORD
import com.pinleen.mobile.utils.Constants.MIN_LENGTH_PASSWORD
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository()
     val responseLogin = MutableLiveData<Response<ResponsePIK>>()
    val mapAuth = HashMap<String, String>()

    init {
        mapAuth["cu-x-server"] = "8jfy572hf74xfhhg23C343u5u2jfw3240"
        mapAuth["Content-Type"] = "application/json"
      //  mapAuth["Authorization"] = "Bearer $PIK"
    }
    fun validateAndLogin(
        email: String,
        password: String,
        listener: ValidEmailPasswordListener
    ) {

        val regexUppercase = Regex("[A-Z]")
        val regexLowercase = Regex("[a-z]")
        val regexNumbers = Regex("[0-9]")

        val isValidEmail = EMAIL_ADDRESS.matcher(email).matches()
        val hasUppercase = regexUppercase.containsMatchIn(password)
        val hasLowerCase = regexLowercase.containsMatchIn(password)
        val hasNumber = regexNumbers.containsMatchIn(password)
        val isValidLength = password.length in MIN_LENGTH_PASSWORD..MAX_LENGTH_PASSWORD

        if (isValidEmail &&
            hasUppercase &&
            hasLowerCase &&
            hasNumber &&
            isValidLength
        ) {
            viewModelScope.launch {
                callLogin(RequestLogin(email, password))
            }
        } else {
            listener.isValid(
                isValidEmail,
                hasUppercase,
                hasLowerCase,
                hasNumber,
                isValidLength
            )
        }
    }


    private suspend fun callLogin(paramLogin: RequestLogin) {
        responseLogin.value = repository.login(paramLogin, mapAuth)
    }
}

