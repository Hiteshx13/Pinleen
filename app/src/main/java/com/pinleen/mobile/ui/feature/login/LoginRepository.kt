package com.pinleen.mobile.ui.feature.login

import com.pinleen.mobile.data.models.request.*
import com.pinleen.mobile.data.models.response.ResponsePIK
import com.pinleen.mobile.data.network.retrofit.PinleenAPI
import com.pinleen.mobile.data.network.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository {

    private val registerApi: PinleenAPI =
        RetrofitHelper.getInstance().create(PinleenAPI::class.java)



    suspend fun login(
        param: RequestLogin,
        mapAuth: Map<String, String>
    ) =
        withContext(Dispatchers.IO) {
            registerApi.login(mapAuth, param)
        }

}

