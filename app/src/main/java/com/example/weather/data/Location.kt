package com.example.weather.data

/*
 * Data class to hold Location Data. Modeled from https://openweathermap.org/api/geocoding-api .
 * Excluded local_names since I will only be displaying in English.
 */
data class Location(
    val country: String = "",
    val lat: Double,
    val lon: Double,
    val name: String = "",
    val state: String = "",
    val zip: String = ""
)