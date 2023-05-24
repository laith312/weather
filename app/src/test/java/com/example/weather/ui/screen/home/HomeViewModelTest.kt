package com.example.weather.ui.screen.home

import MainDispatcherRule
import android.content.Context
import com.example.weather.MockLocationRepository
import com.example.weather.WeatherApplication
import com.example.weather.data.Location
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockLocationRepository = MockLocationRepository()

    @Before
    fun setUp() {
        mockLocationRepository.clearData()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun addDefaultLocation() {
    }

    @Test
    fun `fetchSavedLocations_verifySavedData`() = runTest {
        // 1. Create a new HomeViewModel
        // 2. Add some location data to the repository
        // 3. call fetchSavedLocation
        // 4. verify data
        WeatherApplication.appContext = Mockito.mock(Context::class.java)

        val app = Mockito.mock(WeatherApplication::class.java)
        app.onCreate()

        val homeViewModel = HomeViewModel()
        homeViewModel.locationRepository = mockLocationRepository

        val location = Location(lat = 123.0, lon = 987.0)
        mockLocationRepository.saveLocation(location)

        TestScope().launch {
            homeViewModel.fetchSavedLocations()
            advanceUntilIdle()
            assertSame(
                "Location Data not fetched correctly",
                location, homeViewModel.savedLocations[0]
            )
        }
    }

    @Test
    fun `fetchWeatherData$app_debug`() {
    }

    @Test
    fun clearLocations_removesAllDisplayedLocations() = runTest {
        // 1. Create a new HomeViewModel
        // 2. Add some Location data to the location list
        // 3. call Clear Locations
        // 4. Verify the data is cleared
        WeatherApplication.appContext = Mockito.mock(Context::class.java)

        val app = Mockito.mock(WeatherApplication::class.java)
        app.onCreate()

        val homeViewModel = HomeViewModel()
        homeViewModel.locationRepository = mockLocationRepository

        val location = Location(lat = 123.0, lon = 987.0)
        mockLocationRepository.saveLocation(location)

        TestScope().launch {
            homeViewModel.fetchSavedLocations()
            advanceUntilIdle()
            homeViewModel.clearLocations()
            advanceUntilIdle()
            assertSame(
                "Clear Location did not clear the data",
                0,
                homeViewModel.savedLocations.size
            )
        }
    }
}