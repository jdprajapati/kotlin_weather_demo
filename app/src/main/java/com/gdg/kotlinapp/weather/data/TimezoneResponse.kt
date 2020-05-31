package com.gdg.kotlinapp.weather.data

import androidx.annotation.Keep

@Keep
data class TimezoneResponse(
    val abbreviation: String = "",
    val countryCode: String = "",
    val countryName: String = "",
    val dst: String = "",
    val formatted: String = "",
    val gmtOffset: Int = 0,
    val message: String = "",
    val nextAbbreviation: Any = Any(),
    val status: String = "",
    val timestamp: Int = 0,
    val zoneEnd: Any = Any(),
    val zoneName: String = "",
    val zoneStart: Int = 0
)