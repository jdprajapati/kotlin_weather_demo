package com.gdg.kotlinapp.api

import com.gdg.kotlinapp.weather.data.TimezoneResponse
import com.gdg.kotlinapp.weather.data.WeatherResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCallInterface {

    /* get timezone api */
    @GET("https://api.timezonedb.com/v2.1/get-time-zone?format=json&key=AAHLCYFT7MLW&by=position")
    fun getTimezone(@Query("lat") lat : String, @Query("lng") lng : String): Call<TimezoneResponse>

    @GET("https://api.openweathermap.org/data/2.5/weather?units=&appid=6592d24a33ae13c2ac1401db99732c61")
    fun getWeather(@Query("lat") lat : String, @Query("lon") lng : String): Call<WeatherResponse>

}