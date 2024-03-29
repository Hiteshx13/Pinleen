package com.pinleen.mobile.ui.feature.signup

import com.pinleen.mobile.data.models.request.*
import com.pinleen.mobile.data.models.response.ResponsePIK
import com.pinleen.mobile.data.network.retrofit.PinleenAPI
import com.pinleen.mobile.data.network.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SignUpRepository {

    private val registerApi: PinleenAPI =
        RetrofitHelper.getInstance().create(PinleenAPI::class.java)
    val map = mapOf("cu-x-server" to "8jfy572hf74xfhhg23C343u5u2jfw3240")


    suspend fun register(): Response<ResponsePIK> = withContext(Dispatchers.IO) {
        registerApi.register(map)
    }
    suspend fun deleteAccount(email:String): Response<ResponsePIK> = withContext(Dispatchers.IO) {
        registerApi.deleteAccount(email)
    }

    suspend fun registerEmailPassword(
        param: RequestRegisterEmail,
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.startRegisterUser(mapAuth, param)
        }
    suspend fun registerUserNameAndPhone(
        param: RequestRegisterNameAndPhone,
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.registerUserNameAndPhone(mapAuth, param)
        }

    suspend fun verifyEmailOTP(
        param: RequestVerifyEmailOTP,
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.verifyEmailOTP(mapAuth, param)
        }
    suspend fun verifyMobileOTP(
        param: RequestVerifyMobileOTP,
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.verifyMobileOTP(mapAuth, param)
        }
    suspend fun callResendEmailOTP(
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.callResendEmailOTP(mapAuth)
        }

    suspend fun callResendMobileOTP(
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.callResendMobileOTP(mapAuth)
        }
}

