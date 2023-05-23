package com.example.weather.ui.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.WeatherApplication
import com.example.weather.data.Location
import com.example.weather.data.LocationSearch
import com.example.weather.data.WeatherCondition
import com.example.weather.repository.OpenWeatherRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


class HomeViewModel() : ViewModel() {
    private var savedLocations = mutableStateListOf<Location>()
        private set

    var weatherConditions = mutableStateListOf<WeatherCondition>()
        private set

    var errorMessage = ""
        private set

    private val handler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            // This block here catches errors while fetching network data.
            // Skipping for time
            Log.e(HomeViewModel::class.simpleName, "Error fetching data: ${throwable.message}")
            //TODO handle api exceptions
            errorMessage = throwable.message.toString()
        }
    }

    /**
     * If permissions were granted we show the current location then fetch existing searches.
     */
    fun addDefaultLocation(defaultLocation: Location?) {
        savedLocations.clear()
        weatherConditions.clear()
        defaultLocation?.let {
            savedLocations.add(it)
        }
        fetchSavedLocations()
    }

    /**
     * Fetch the saved location for our "Repository" <- currently just a dataStore.
     * Also, the repository would be injected using Koin dependency injection.
     */
    private fun fetchSavedLocations() {
        val dataStoreKey = stringPreferencesKey("locations_key")
        // TODO RoomDB - This should be fetched from a RoomDB
        viewModelScope.launch {
            WeatherApplication.dataStore.edit { locations ->
                val preferences = WeatherApplication.dataStore.data.first()
                val current = Gson().fromJson(preferences[dataStoreKey], LocationSearch::class.java)
                current?.let {
                    savedLocations.addAll(it)
                }
                fetchWeatherData()
            }
        }
    }

    /**
     * Use the saved locations held by the ViewModel to query the weather data for each location
     */
    private fun fetchWeatherData() {
        viewModelScope.launch(handler) {
            weatherConditions.clear()
            for (location in savedLocations) {
                // TODO make OpenWeatherRepository injectable with Koin. It can be passed to the viewModel
                val response =
                    OpenWeatherRepository.instance.getWeatherConditionData(
                        location.lat.toString(),
                        location.lon.toString()
                    )
                        .awaitResponse()
                if (response.isSuccessful) {
                    val result: WeatherCondition? = response.body()
                    result?.let {
                        weatherConditions.add(result)
                    }

                } else {
                    Log.e(HomeViewModel::class.java.simpleName, "Error: ${response.message()}")
                    throw Exception(response.errorBody()?.string() ?: "")
                }
            }
        }
    }
}
