package com.gdg.kotlinapp.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gdg.kotlinapp.BuildConfig
import com.gdg.kotlinapp.R
import com.gdg.kotlinapp.databinding.ActivityMainBinding
import com.gdg.kotlinapp.utils.Utils
import com.gdg.kotlinapp.utils.Utils.Companion.getIconDrawableFromDailyWeather
import com.gdg.kotlinapp.utils.Utils.Companion.isNetworkAvailable
import com.gdg.kotlinapp.weather.viewmodel.WeatherViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding : ActivityMainBinding
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Represents a geographical location.
     */
    protected var mLastLocation: Location? = null

    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setSupportActionBar(binding.toolbar)

    }

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result
//                    callTimezoneDetails()
                    callGetWeatherDetails(true)

                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    Utils.showToast(getString(R.string.no_location_detected))
                }
            }
    }

    private fun callTimezoneDetails() {
        if (isNetworkAvailable()) {
            showProgress(true)

            viewModel.callTimezoneDetails(mLastLocation?.latitude.toString(), mLastLocation?.longitude.toString())
            viewModel.getTimeZoneLiveData().observe(this, Observer {
                showProgress(false)
                if (it != null) {
                    Log.d(TAG, "callTimezoneDetails() called: "+it.countryName)

                    callGetWeatherDetails(true)
                }
                else{
                    Utils.showToast("Data not found!")
                }
            })
        }
        else{
            Utils.showToast(getString(R.string.internet_err))
        }

    }

    private fun callGetWeatherDetails(isLoader: Boolean) {
        if (isNetworkAvailable()) {
            showProgress(isLoader)

            viewModel.callWeatherDetails(mLastLocation?.latitude.toString(), mLastLocation?.longitude.toString())
            viewModel.getWeatherLiveData().observe(this, Observer {
                showProgress(false)
                if (it != null) {
                    Log.d(TAG, "callTimezoneDetails() called: "+it.weather.get(0).main)

                    binding.tvLastUpdate.text = getString(R.string.last_update, Utils.getDateTime(it.dt))
                    binding.tvPressure.text = getString(R.string.pressure, it.main.pressure.toString())
                    binding.tvHumidity.text = it.main.humidity.toString() + "%"
                    binding.tvVisibility.text = getString(R.string.visibility, it.visibility.toString())
                    binding.tvWindSpeed.text = getString(R.string.wind, it.wind.speed.toString())
                    binding.tvWeatherStatus.text = it.weather.get(0).main
                    binding.tvTemperature.text = (DecimalFormat("#.#").format((it.main.temp - 273.15)))+"°C"
                    binding.ivWeatherIcon.setImageResource(getIconDrawableFromDailyWeather(it.weather.get(0).icon))
                    binding.layoutDetails.visibility = View.VISIBLE

                }
                else{
                    Utils.showToast("Data not found!")
                }
            })
        }
        else{
            Utils.showToast(getString(R.string.internet_err))
        }

    }

    private fun showProgress(isShow: Boolean) {
        binding.progressBar.visibility = if (isShow)  View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                if (!checkPermissions()) {
                    requestPermissions()
                } else {
                    getLastLocation()
                }
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_COARSE_LOCATION)

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")

            Utils.showAlertDialog(applicationContext, "", getString(R.string.permission_rationale), object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    startLocationPermissionRequest()
                }
            })

        } else {
            Log.i(TAG, "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                Utils.showAlertDialog(
                    applicationContext,
                    "",
                    getString(R.string.permission_denied_explanation),
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    })

            }
        }
    }



}