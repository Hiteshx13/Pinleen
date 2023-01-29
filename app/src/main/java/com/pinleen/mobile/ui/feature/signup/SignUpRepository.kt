package com.pinleen.mobile.ui.feature.signup

import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import com.pinleen.mobile.data.network.retrofit.PinleenAPI
import com.pinleen.mobile.data.network.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Callback
import retrofit2.Response

class SignUpRepository {

    private val registerApi: PinleenAPI =
        RetrofitHelper.getInstance().create(PinleenAPI::class.java)

    val map = mapOf("cu-x-server" to "8jfy572hf74xfhhg23C343u5u2jfw3240")


    suspend fun register(): Response<ResponseStartRegistration> = withContext(Dispatchers.IO) {
        registerApi.register(map)
    }

    suspend fun registerEmailPassword(
        param: RequestRegisterEmail,
        mapAuth:Map<String,String>

    ) =
        withContext(Dispatchers.IO) {
            registerApi.startRegisterUser(mapAuth, param)
        }
}

