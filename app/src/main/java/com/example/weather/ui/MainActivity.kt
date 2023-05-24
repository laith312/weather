package com.example.weather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.weather.data.Location
import com.example.weather.ui.theme.WeatherTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setupContent(isPermissionGranted = true)
        } else {
            // NOTE: We should only be checking the permission when we are ready to use it. Also,
            // I am not showing a Rationale UI for the sake of time.
            Log.d("Permission", "Permissions not yet granted. Requesting Permission.")
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        return
    }

    /**
     * After we have determines if permissions are we can set the content with either the users location
     * or pass null to indicate no default location.
     */
    private fun setupContent(isPermissionGranted: Boolean) {
        var currentLocation: Location? = null
        val defaultLocationTask = getDefaultLocation(isPermissionGranted)

        if (isPermissionGranted && defaultLocationTask != null) {
            defaultLocationTask.addOnSuccessListener {
                if (it != null) {
                    currentLocation = Location(lat = it.latitude, lon = it.longitude)
                }
                lifecycleScope.launch(Dispatchers.Main) {
                    setContent {
                        WeatherTheme {
                            MainActivityNavigation(currentLocation)
                        }
                    }
                }
            }
        } else {
            lifecycleScope.launch(Dispatchers.Main) {
                setContent {
                    WeatherTheme {
                        MainActivityNavigation(currentLocation)
                    }
                }
            }
        }

    }

    /**
     * If Permissions are granted we return the location Task<Location> for you to retrieve the current
     * location. If no permissions are granted null is returned.
     */
    @SuppressLint("MissingPermission")
    fun getDefaultLocation(isGranted: Boolean): Task<android.location.Location>? {
        return if (isGranted) {
            val locationClient = LocationServices.getFusedLocationProviderClient(this)
            locationClient.lastLocation
        } else null
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            setupContent(isGranted)

            // NOTE: If the permission was not granted I would send them to a rationale screen.
        }
}