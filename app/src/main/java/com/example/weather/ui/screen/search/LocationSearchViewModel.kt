package com.example.weather.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.WeatherApplication
import com.example.weather.data.Location
import com.example.weather.data.LocationSearch
import com.example.weather.repository.OpenWeatherRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


class LocationSearchViewModel : ViewModel() {
    // Stores the city text field value
    var city by mutableStateOf("")
        private set

    // Stores the state text field value
    var state by mutableStateOf("")
        private set

    // Stores the country text field value - default "US"
    var country by mutableStateOf("US")
        private set

    // Stores the zip code text field value
    var zipCode by mutableStateOf("")
        private set

    // Stores the result of a geolocation search
    var locationResult by mutableStateOf(listOf<Location>())
        private set

    var error by mutableStateOf("")
        private set

    private val handler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch(Dispatchers.Main) {
            error = throwable.message.toString()
        }
    }

    fun updateCity(city: String) {
        this.city = city
    }

    fun updateCountry(countryCode: String) {
        this.country = countryCode
    }

    fun updateState(state: String) {
        this.state = state
    }

    fun updateZipcode(zipCode: String) {
        this.zipCode = zipCode
    }

    fun searchZipCode(zipCode: String) {
        viewModelScope.launch(handler) {
            require(zipCode.length == 5) { "incorrect zipcode" }
            // TODO make OpenWeatherRepository injectable with Koin. It can be passed to the viewModel
            val response = OpenWeatherRepository.instance.getLocationByZip(zipCode).awaitResponse()
            if (response.isSuccessful) {
                val result: Location? = response.body()
                result?.let {
                    locationResult = listOf(it)
                }

            } else {
                throw Exception(response.errorBody()?.string() ?: "")
            }
        }
    }

    fun searchByCity() {
        viewModelScope.launch(handler) {
            require(city.isNotEmpty()) { "missing city" }
            val response =
                OpenWeatherRepository.instance.getLocationByCityName("$city,$state,$country")
                    .awaitResponse()
            if (response.isSuccessful) {
                locationResult = response.body()!!.toList()
            } else {
                throw Exception(response.errorBody()?.string() ?: "")
            }
        }
    }

    fun addLocation(location: Location) {
        // TODO - RoomDB - This should be written to a RoomDB
        val dataStoreKey = stringPreferencesKey("locations_key")
        viewModelScope.launch {
            WeatherApplication.dataStore.edit { locations ->
                val preferences = WeatherApplication.dataStore.data.first()
                val current = Gson().fromJson(preferences[dataStoreKey], LocationSearch::class.java)
                    ?: LocationSearch()
                current.add(location)
                locations[dataStoreKey] = Gson().toJson(current)
            }
        }
    }
}