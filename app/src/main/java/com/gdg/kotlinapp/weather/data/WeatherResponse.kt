package com.gdg.kotlinapp.weather.data


import androidx.annotation.Keep

@Keep
data class WeatherResponse(
    val base: String = "",
    val clouds: Clouds = Clouds(),
    val cod: Int = 0,
    val coord: Coord = Coord(),
    val dt: Long = 0,
    val id: Int = 0,
    val main: Main = Main(),
    val name: String = "",
    val sys: Sys = Sys(),
    val timezone: Long = 0,
    val visibility: Int = 0,
    val weather: List<Weather> = listOf(),
    val wind: Wind = Wind()
) {
    @Keep
    data class Clouds(
        val all: Int = 0
    )

    @Keep
    data class Coord(
        val lat: Double = 0.0,
        val lon: Double = 0.0
    )

    @Keep
    data class Main(
        val feels_like: Double = 0.0,
        val humidity: Int = 0,
        val pressure: Int = 0,
        val temp: Double = 0.0,
        val temp_max: Double = 0.0,
        val temp_min: Double = 0.0
    )

    @Keep
    data class Sys(
        val country: String = "",
        val id: Int = 0,
        val sunrise: Int = 0,
        val sunset: Int = 0,
        val type: Int = 0
    )

    @Keep
    data class Weather(
        val description: String = "",
        val icon: String = "",
        val id: Int = 0,
        val main: String = ""
    )

    @Keep
    data class Wind(
        val deg: Int = 0,
        val gust: Double = 0.0,
        val speed: Double = 0.0
    )
}