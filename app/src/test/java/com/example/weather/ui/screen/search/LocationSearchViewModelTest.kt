package com.example.weather.ui.screen.search

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.weather.WeatherApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class LocationSearchViewModelTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun addLocationToEmptyDataStore() = runTest {

        val mockDataStore = mock<DataStore<Preferences>>() {
            on { } doReturn {    }
        }

        val mockContext = mock<Context> {
            //on { getString(R.string.a) } doReturn ""
        }



        // check DataStore
        val dataStoreKey = stringPreferencesKey("locations_key")

//        val testDataStore: DataStore<Preferences> =
//            PreferenceDataStoreFactory.create(
//                produceFile = { mockContext.preferencesDataStore(name = "data") }
//            )

        launch {
            //Assert.assertEquals("7", "6")
            mockDataStore.edit { locations ->
                val preferences = WeatherApplication.dataStore.data.first()
                val current = preferences[dataStoreKey]
                Assert.assertEquals("7", "6")
            }

        }

        //advanceUntilIdle()

        Assert.assertEquals("7", "7")
        // add a location

        // recheck to see if datastore added location


    }

    @Test
    fun addLocationToNonEmptyDataStore() {
        // check DataStore

        // add a location

        // recheck to see if datastore added location


    }
}