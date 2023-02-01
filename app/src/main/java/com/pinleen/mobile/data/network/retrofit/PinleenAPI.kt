package com.pinleen.mobile.data.network.retrofit

import com.pinleen.mobile.data.models.request.RequestRegisterEmail
import com.pinleen.mobile.data.models.request.RequestRegisterNameAndPhone
import com.pinleen.mobile.data.models.request.RequestVerifyEmailOTP
import com.pinleen.mobile.data.models.request.RequestVerifyMobileOTP
import com.pinleen.mobile.data.models.response.ResponseStartRegistration
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface PinleenAPI {

    @GET("android-xrqv981?lang=en")
    suspend fun register(@HeaderMap headers: Map<String, String>): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun startRegisterUser(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterEmail
    ): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun callResendEmailOTP(
        @HeaderMap headers: Map<String, String>
    ): Response<ResponseStartRegistration>

    @POST("new-user")
    suspend fun verifyEmailOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestVerifyEmailOTP
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