package com.gdg.kotlinapp.api

import com.gdg.kotlinapp.weather.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCallInterface {

    @GET("weather?units=&appid=7595ac2684945c16da930856fd0e12bf")
    fun getWeather(@Query("lat") lat : String, @Query("lon") lng : String): Call<WeatherResponse>

}