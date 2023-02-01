package com.pinleen.mobile.ui.feature.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinleen.mobile.data.models.request.RequestVerifyEmailOTP
import com.pinleen.mobile.data.models.request.RequestVerifyMobileOTP
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpVerificationViewModel : ViewModel() {

    private val repository = SignUpRepository()
    val responseVerifyEmailOTP = MutableLiveData<Response<ResponseStartRegistration>>()
    val responseResendEmailOTP = MutableLiveData<Response<ResponseStartRegistration>>()
    val responseVerifyMobileOTP = MutableLiveData<Response<ResponseStartRegistration>>()
    val responseResendMobileOTP = MutableLiveData<Response<ResponseStartRegistration>>()

    fun validateOTP(
        otp: String
    ): Boolean {
        return (!otp.isNullOrEmpty() && otp.length == 4)
    }


    fun verifyEmailOTP(param: RequestVerifyEmailOTP, mapAuth: Map<String, String>) {

        viewModelScope.launch {
            responseVerifyEmailOTP.value = repository.verifyEmailOTP(param, mapAuth)
        }
    }
    fun verifyMobileOTP(param: RequestVerifyMobileOTP, mapAuth: Map<String, String>) {

        viewModelScope.launch {
            responseVerifyMobileOTP.value = repository.verifyMobileOTP(param, mapAuth)
        }
    }

    fun callResendEmailOTP(mapAuth: Map<String, String>) {
        viewModelScope.launch {
            responseResendMobileOTP.value = repository.callResendEmailOTP(mapAuth)
        }
    }
}

