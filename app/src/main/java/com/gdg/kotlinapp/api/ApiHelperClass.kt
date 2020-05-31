package com.gdg.kotlinapp.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiHelperClass {

    companion object {

        private var instance: ApiCallInterface? = null
        private var apiCallInterface: ApiCallInterface? = null
        private var BASE_URL: String? = "https://api.timezonedb.com"

        fun getInstance(): ApiCallInterface {
            val timeOut: Long = 30 * 1000

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                .readTimeout(timeOut, TimeUnit.MILLISECONDS).writeTimeout(timeOut, TimeUnit.MILLISECONDS).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            instance = retrofit.create(ApiCallInterface::class.java)

            return instance as ApiCallInterface
        }


        fun getApiInstanceWithToken(): ApiCallInterface {
            if (apiCallInterface == null) {
                val timeout = (30 * 1000).toLong()
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                    .readTimeout(timeout, TimeUnit.MILLISECONDS).writeTimeout(timeout, TimeUnit.MILLISECONDS).build()

                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                apiCallInterface = retrofit.create(ApiCallInterface::class.java)
            }
            return apiCallInterface as ApiCallInterface
        }
    }
}