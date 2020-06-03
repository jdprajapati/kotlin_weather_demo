package com.gdg.kotlinapp.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gdg.kotlinapp.api.ApiHelperClass
import com.gdg.kotlinapp.weather.data.WeatherResponse
import retrofit2.Call
import retrofit2.Response

class WeatherRepository private constructor(){

    val TAG = WeatherRepository::class.java.simpleName

    /* singleton */
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: WeatherRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance
                    ?: WeatherRepository().also { instance = it }
            }
    }

    /* Get Weather API */
    fun callWeatherDetails(latitude: String, longitude: String): MutableLiveData<WeatherResponse> {

        val data = MutableLiveData<WeatherResponse>()
        ApiHelperClass.getInstance().getWeather(latitude, longitude)
            .enqueue(object : retrofit2.Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    val responseModel = response.body()
                    data.value = responseModel
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    data.value = null
                }
            })

        return data

        return data
    }


}