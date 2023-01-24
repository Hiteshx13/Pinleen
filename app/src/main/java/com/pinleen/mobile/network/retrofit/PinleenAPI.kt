package com.pinleen.mobile.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PinleenAPI {
    @GET("/user")
    suspend fun getUser() : Response<String>
}