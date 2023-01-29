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
//"cu-x-server:8jfy572hf74xfhhg23C343u5u2jfw3240,Authorization: Bearer"
//    @Headers("cu-x-server:8jfy572hf74xfhhg23C343u5u2jfw3240")
    @GET("android-xrqv981?lang=es")
    suspend fun register(@HeaderMap headers: Map<String, String>): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun startRegisterUser(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterEmail
    ): Response<ResponseStartRegistration>


    /*
    @GET("android-xrqv981?lang=en")
    fun register(
        @HeaderMap headers: Map<String, String>,
    ): ResponseStartRegistration



*/
}