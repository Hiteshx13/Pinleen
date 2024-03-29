package com.pinleen.mobile.data.network.retrofit

import com.pinleen.mobile.data.models.request.*
import com.pinleen.mobile.data.models.response.ResponseLogin
import com.pinleen.mobile.data.models.response.ResponsePIK
import retrofit2.Response
import retrofit2.http.*

interface PinleenAPI {

    @GET("android-xrqv981?lang=en")
    suspend fun register(@HeaderMap headers: Map<String, String>): Response<ResponsePIK>

    @DELETE("my-info/{email}")
    suspend fun deleteAccount(@Path ("email") email:String): Response<ResponsePIK>

    @POST("new-user")
    suspend fun startRegisterUser(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterEmail
    ): Response<ResponsePIK>

    @PUT("new-user")
    suspend fun verifyEmailOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestVerifyEmailOTP
    ): Response<ResponsePIK>

    @PATCH("new-user")
    suspend fun callResendEmailOTP(
        @HeaderMap headers: Map<String, String>
    ): Response<ResponsePIK>

  @PATCH("users/name-phone")
    suspend fun callResendMobileOTP(
        @HeaderMap headers: Map<String, String>
    ): Response<ResponsePIK>


    @POST("users/name-phone")
    suspend fun registerUserNameAndPhone(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestRegisterNameAndPhone
    ): Response<ResponsePIK>

    @PUT("users/name-phone")
    suspend fun verifyMobileOTP(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestVerifyMobileOTP
    ): Response<ResponsePIK>

    @POST("users/user/login")
    suspend fun login(
        @HeaderMap headers: Map<String, String>,
        @Body param: RequestLogin
    ): Response<ResponseLogin>

}