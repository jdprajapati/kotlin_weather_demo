package com.gdg.kotlinapp.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.gdg.kotlinapp.R
import com.gdg.kotlinapp.WeatherApplication
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    companion object {

        fun showToast(msg: String) {
            val toast = Toast.makeText(
                WeatherApplication.getApplicationContext(),
                msg,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.BOTTOM, 0, 120)
            toast.show()
        }

        fun showAlertDialog(
            context: Context,
            title: String,
            msg: String,
            onClickListener: DialogInterface.OnClickListener
        ) {
            var dialog: AlertDialog? = null
            val alertDialog = AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(msg);
            alertDialog.setPositiveButton(
                android.R.string.ok,
                object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        dialog?.dismiss()
                        onClickListener.onClick(p0, p1)
                    }

                })

            if(!TextUtils.isEmpty(title)){
                alertDialog.setTitle(title)
            }

            dialog = alertDialog.create()
            dialog.show()
        }



        fun isNetworkAvailable(): Boolean {
            val cm =
                WeatherApplication.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return (netInfo != null && netInfo.isConnectedOrConnecting
                    && cm.activeNetworkInfo.isAvailable
                    && cm.activeNetworkInfo.isConnected)
        }

        fun isLocationPermissionGranted(context: Context): Boolean {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            )
                return true
            return false
        }

        @DrawableRes
        fun getIconDrawableFromDailyWeather(icon: String): Int {
            return WeatherApplication.getApplicationContext().resources.getIdentifier(
                "weather_icon_$icon",
                "drawable",
                WeatherApplication.getApplicationContext().packageName
            ).takeIf { it != 0 } ?: R.drawable.weather_icon_null
        }

        fun getDateTime(s: Long): String? {
            try {
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                return sdf.format(Date(s * 1000))
            } catch (e: Exception) {
                return e.toString()
            }
        }


    }






}