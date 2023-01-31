package com.pinleen.mobile.data.network.retrofit

import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface PinleenAPI {

    @GET("android-xrqv981?lang=es")
    suspend fun register(@HeaderMap headers: Map<String, String>): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun startRegisterUser(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterEmail
    ): Response<ResponseStartRegistration>

}