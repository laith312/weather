package com.example.weather.ui.screen

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object SearchScreen : Screen("location_search_screen")
    object DetailScreen : Screen("weather_detail_screen")
}