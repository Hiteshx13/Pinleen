package com.pinleen.mobile.data.network.retrofit

import com.pinleen.mobile.data.models.request.*
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import retrofit2.Response
import retrofit2.http.*

interface PinleenAPI {

    @GET("android-xrqv981?lang=en")
    suspend fun register(@HeaderMap headers: Map<String, String>): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun startRegisterUser(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterEmail
    ): Response<ResponseStartRegistration>

    @PUT("new-user")
    suspend fun verifyEmailOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestVerifyEmailOTP
    ): Response<ResponseStartRegistration>

    @PATCH("new-user")
    suspend fun callResendEmailOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestResendEmailOTP
    ): Response<ResponseStartRegistration>


    @POST("users/:uuid/name-phone")
    suspend fun registerUserNameAndPhone(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterNameAndPhone
    ): Response<ResponseStartRegistration>

    @POST("users/:uuid/name-phone")
    suspend fun verifyMobileOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestVerifyMobileOTP
    ): Response<ResponseStartRegistration>

}