package com.gdg.kotlinapp.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gdg.kotlinapp.weather.data.WeatherResponse

class WeatherViewModel : ViewModel(){

    private lateinit var weatherLivedata: LiveData<WeatherResponse>

    // call timezone api
    fun callWeatherDetails(latitude: String, longitude: String) {
        weatherLivedata = WeatherRepository.getInstance().callWeatherDetails(latitude, longitude)
    }

    fun getWeatherLiveData():LiveData<WeatherResponse>{
        return weatherLivedata
    }

}
