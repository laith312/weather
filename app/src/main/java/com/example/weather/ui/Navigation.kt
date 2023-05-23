package com.example.weather.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather.data.Location
import com.example.weather.ui.screen.Screen
import com.example.weather.ui.screen.details.DetailScreenComposable
import com.example.weather.ui.screen.home.HomeScreenComposable
import com.example.weather.ui.screen.search.SearchScreenComposable


@Composable
fun MainActivityNavigation(defaultLocation: Location?) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            HomeScreenComposable(navController = navController, defaultLocation)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreenComposable(navController = navController)
        }
        composable(route = Screen.DetailScreen.route) {
            DetailScreenComposable(navController = navController)
        }
    }
}
