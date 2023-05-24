package com.example.weather

import com.example.weather.data.Location
import com.example.weather.repository.SavedLocationRepositoryInterface

/**
 * Used to simulate the DataStore in the main app
 */
class MockLocationRepository : SavedLocationRepositoryInterface {

    val data = mutableListOf<Location>()

    override suspend fun saveLocation(location: Location, callBack: () -> Unit) {
        data.add(location)
        callBack.invoke()
    }

    override suspend fun getSavedLocations(callBack: (List<Location>) -> Unit) {
        callBack.invoke(data)
    }

    override suspend fun clearSavedLocations(callBack: () -> Unit) {
        data.clear()
        callBack.invoke()
    }

    fun clearData() {
        data.clear()
    }
}