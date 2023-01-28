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
    val mapAuth = mapOf("cu-x-server" to "8jfy572hf74xfhhg23C343u5u2jfw3240",
        "Authorization" to "Bearer <eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkZXZpY2UiOiJhbmRyb2lkIiwidXVpZCI6Ijk1YWU4Mzc3LTc0MjEtNDhlNi1hYzZjLWY4ZjZjNmEzYTJkMCIsImxhbmciOiJlcyIsInByb2NjIjowLCJpYXQiOjE2NzQ4NjQ1NTAsImV4cCI6MTY3NDg3NDU0OX0.rR3_2G9_rCBHssbpBv7np6wY-ubLcEq3ii-pu_-7q4Q>"
    )

    suspend fun register(): ResponseStartRegistration = withContext(Dispatchers.IO) {
        registerApi.register(map)
    }

    suspend fun registerEmailPassword(param: RequestRegisterEmail): ResponseStartRegistration =
        withContext(Dispatchers.IO) {
            registerApi.startRegisterUser(mapAuth, param)
        }
}

