package com.pinleen.mobile.data.network.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitHelper {

    private const val BASE_URL = "https://register.pinleen.com/"

    fun getInstance(): Retrofit {
        val logging = HttpLoggingInterceptor()

        logging.apply { logging.level = HttpLoggingInterceptor.Level.BODY }
        val interCeptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .header("cu-x-server", "8jfy572hf74xfhhg23C343u5u2jfw3240")
                val request: Request = requestBuilder.build()
                return chain.proceed(request)
            }
        }
        val httpClient =
            OkHttpClient.Builder().addInterceptor(logging).addInterceptor(interCeptor).build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }
}