package com.example.weather.ui.screen.home

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data.Location
import com.example.weather.data.WeatherCondition
import com.example.weather.network.OpenWeatherMapAPI
import com.example.weather.repository.LocationRepository
import com.example.weather.repository.OpenWeatherRepository
import com.example.weather.repository.SavedLocationRepositoryInterface
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


class HomeViewModel() : ViewModel() {
    // Use Koin to inject these
    var locationRepository: SavedLocationRepositoryInterface = LocationRepository()
    var weatherRepository: OpenWeatherMapAPI = OpenWeatherRepository.instance

    var savedLocations = mutableStateListOf<Location>()
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
    internal fun fetchSavedLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.getSavedLocations {
                viewModelScope.launch(Dispatchers.Main) {
                    savedLocations.addAll(it)
                    fetchWeatherData()
                }
            }
        }
    }

    /**
     * Use the saved locations held by the ViewModel to query the weather data for each location
     */
    internal fun fetchWeatherData() {
        viewModelScope.launch(handler) {
            weatherConditions.clear()
            for (location in savedLocations) {
                // TODO make OpenWeatherRepository injectable with Koin. It can be passed to the viewModel
                val response =
                    weatherRepository.getWeatherConditionData(
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

    fun clearLocations() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.clearSavedLocations() {
                addDefaultLocation(null)
            }
        }
    }

    /**
     * Help method of testing to add locations manually to the list.
     */
    fun addLocation(location: Location) {
        savedLocations.add(location)
    }
}
