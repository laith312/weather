package com.example.weather.ui.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.weather.ui.theme.WeatherTheme

@Composable
fun DetailScreenComposable(navController: NavController) {
    WeatherTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .background(Color.Cyan)
        ) {
            Text(text = "DetailScreen", fontSize = 50.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DetailScreenComposable(rememberNavController())
}