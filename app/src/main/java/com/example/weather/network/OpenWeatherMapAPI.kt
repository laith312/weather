package com.example.weather.network

import com.example.weather.data.Location
import com.example.weather.data.LocationSearch
import com.example.weather.data.WeatherCondition
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface used for fetching Location and Weather data. Designed for https://api.openweathermap.org/
 * using retrofit
 */
interface OpenWeatherMapAPI {

    /* I am adding the API key here, normally this is bad practice and should go in a HEADER but
     * openweathermap.org does not support api key in the header so we append them to the URL
     */

    @GET("geo/1.0/direct?appid=deca5eb02550060ad80ed7a11e365553&limit=5")
    fun getLocationByCityName(@Query("q") locationText: String): Call<LocationSearch>

    @GET("geo/1.0/zip?appid=deca5eb02550060ad80ed7a11e365553")
    fun getLocationByZip(@Query("zip") zipcode: String): Call<Location>

    @GET("data/2.5/weather?appid=deca5eb02550060ad80ed7a11e365553&units=imperial")
    fun  getWeatherConditionData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Call<WeatherCondition>
}