package com.gdg.kotlinapp

import android.app.Application
import android.content.Context
import android.os.Vibrator
import java.util.*

class WeatherApplication : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()


    }

    companion object {
        private var instance: WeatherApplication? = null

        fun getApplicationContext(): Context {
            return instance?.applicationContext!!
        }
    }


}