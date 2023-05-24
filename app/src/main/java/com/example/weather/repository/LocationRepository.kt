package com.example.weather.repository

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.weather.WeatherApplication
import com.example.weather.data.Location
import com.example.weather.data.LocationSearch
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

/**
 * This Repository give access to allow us to save and retrieve Locations added by the user.
 *
 * Normally a Room DB is the way to go, but since I don't have enough time to set it up I will use
 * a dataStore.
 */
class LocationRepository : SavedLocationRepositoryInterface {
    private val dataStoreKey = stringPreferencesKey("locations_key")

    /**
     * Save a Location to the dataStore
     */
    override suspend fun saveLocation(location: Location, callBack: () -> Unit) {
        WeatherApplication.dataStore.edit { locations ->
            val preferences = WeatherApplication.dataStore.data.first()
            val current = Gson().fromJson(preferences[dataStoreKey], LocationSearch::class.java)
                ?: LocationSearch()
            current.add(location)
            locations[dataStoreKey] = Gson().toJson(current)
            callBack.invoke()
        }
    }

    /**
     * Fetch a list of Locations saved in the dataStore
     */
    override suspend fun getSavedLocations(locations: (List<Location>) -> Unit) {
        WeatherApplication.dataStore.edit {
            val preferences = WeatherApplication.dataStore.data.first()
            val current = Gson().fromJson(preferences[dataStoreKey], LocationSearch::class.java)
            locations.invoke(current ?: listOf())
        }
    }

    /**
     * Clear the saved locations in the dataStore
     */
    override suspend fun clearSavedLocations(callBack: () -> Unit) {
        WeatherApplication.dataStore.edit { locations ->
            locations[dataStoreKey] = ""
            callBack.invoke()
        }
    }

}

/**
 * Interface used to save Location Data
 */
interface SavedLocationRepositoryInterface {
    suspend fun saveLocation(location: Location, callBack: () -> Unit = {}) {}
    suspend fun getSavedLocations(locations: (List<Location>) -> Unit = {}) {}
    suspend fun clearSavedLocations(callBack: () -> Unit = {}) {}
}