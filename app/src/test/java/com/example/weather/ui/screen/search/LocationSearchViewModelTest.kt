package com.example.weather.ui.screen.search

import MainDispatcherRule
import android.content.Context
import com.example.weather.MockLocationRepository
import com.example.weather.WeatherApplication
import com.example.weather.data.Location
import com.example.weather.repository.OpenWeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.Cache
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.awaitResponse

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LocationSearchViewModelTest {

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
    fun addLocation_verifyData() {
        // 1. Create a new LocationSearchViewModel
        // 2. Call add location - simulating user clicked from the search results
        // 3. verify the data was added to the repository
        WeatherApplication.appContext = mock(Context::class.java)

        val app = mock(WeatherApplication::class.java)
        app.onCreate()

        val locationSearchViewMode = LocationSearchViewModel()
        locationSearchViewMode.locationRepository = mockLocationRepository

        val location = Location(lat = 123.0, lon = 987.0)
        locationSearchViewMode.addLocation(location)

        Assert.assertSame("", location, mockLocationRepository.data[0])
    }

    @Ignore
    @Test
    fun searchZipCode_verifyLocationResults() = runTest {
        WeatherApplication.appContext = mock(Context::class.java)
        Mockito.`when`(OpenWeatherRepository.buildCache()).thenReturn(mock(Cache::class.java))
        val app = mock(WeatherApplication::class.java)
        app.onCreate()
        //Mockito.`when`(WeatherApplication.appContext.cacheDir).doReturn(mock(File::class.java))




        val ZIP_CODE = "95136"
        val locationSearch: Location = Location(lon = 123.0, lat = 456.0)
        val mockResponse: Response<Location> = Response.success(200, locationSearch)

        Mockito.`when`(OpenWeatherRepository.instance.getLocationByZip(ZIP_CODE).awaitResponse())
            .thenReturn(
                mockResponse
            )

        advanceUntilIdle()
        val locationSearchViewModel = LocationSearchViewModel()
        locationSearchViewModel.searchZipCode("95136")

        Assert.assertEquals(locationSearch, locationSearchViewModel.locationResult[0])
    }
}