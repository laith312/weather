package com.example.weather

import android.app.Application
import android.content.Context
import android.preference.Preference
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class WeatherApplication : Application() {


    private val _dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        dataStore = _dataStore
    }

    companion object {
        lateinit var appContext: Context
        lateinit var dataStore: DataStore<Preferences>
    }
}