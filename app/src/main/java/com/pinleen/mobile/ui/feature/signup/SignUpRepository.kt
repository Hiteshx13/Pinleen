package com.pinleen.mobile.ui.feature.signup

import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import com.pinleen.mobile.data.network.retrofit.PinleenAPI
import com.pinleen.mobile.data.network.retrofit.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignUpRepository {

    private val registerApi: PinleenAPI =
        RetrofitHelper.getInstance().create(PinleenAPI::class.java)

    val map = mapOf("cu-x-server" to "8jfy572hf74xfhhg23C343u5u2jfw3240")
    val mapAuth = mapOf(
        "Authorization" to "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXZpY2UiOiJhbmRyb2lkIiwidXVpZCI6ImFkOWFiMzg3LWQ3YTQtNGQ0Ny1iYzMyLTI2ZDY4ZGQ2ZjAwMSIsImxhbmciOiJlcyIsInByb2NjIjowLCJpYXQiOjE2NzQ3ODUyNDYsImV4cCI6MTY3NDc5NTI0NX0.SzprSI2LQzskvl5b3NrG5yi0PkfhSkhwcSZJVkMVjPc"
    )

    suspend fun register(): ResponseStartRegistration = withContext(Dispatchers.IO) {
        registerApi.register(map)
    }

    suspend fun registerEmailPassword(param: RequestRegisterEmail): ResponseStartRegistration =
        withContext(Dispatchers.IO) {
            registerApi.startRegisterUser(mapAuth, param)
        }
}

