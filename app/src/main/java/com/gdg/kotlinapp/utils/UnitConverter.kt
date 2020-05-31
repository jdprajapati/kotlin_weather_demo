package com.gdg.kotlinapp.utils



object UnitConverter {
  /**
   * Convert [temperatureInKelvin] from [KELVIN] to [to]
   */
  fun convertTemperature(temperatureInKelvin: Double, to: TemperatureUnit): Double {
    return when (to) {
      TemperatureUnit.KELVIN -> temperatureInKelvin
      TemperatureUnit.CELSIUS -> temperatureInKelvin - 272.15
      TemperatureUnit.FAHRENHEIT -> temperatureInKelvin * 9.0 / 5 - 459.67
    }
  }

  /**
   * Convert [speedInMetersPerSecond] from [METERS_PER_SECOND] to [to]
   */
  fun convertSpeed(speedInMetersPerSecond: Double, to: SpeedUnit): Double {
    return when (to) {
      SpeedUnit.METERS_PER_SECOND -> speedInMetersPerSecond
      SpeedUnit.KILOMETERS_PER_HOUR -> 3.6 * speedInMetersPerSecond
      SpeedUnit.MILES_PER_HOUR -> 2.23693629 * speedInMetersPerSecond
    }
  }

  /**
   * Convert [pressureIn_hPa] from [HPA] to [to]
   */
  fun convertPressure(pressureIn_hPa: Double, to: PressureUnit): Double {
    return when (to) {
      PressureUnit.HPA -> pressureIn_hPa
      PressureUnit.MM_HG -> pressureIn_hPa * 0.75006157584566
    }
  }
}