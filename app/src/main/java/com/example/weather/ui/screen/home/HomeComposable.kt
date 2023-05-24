package com.example.weather.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.weather.data.Location
import com.example.weather.data.WeatherCondition
import com.example.weather.ui.screen.Screen

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreenComposable(navController: NavController, defaultLocation: Location?) {
    val viewModel = viewModel<HomeViewModel>()
    viewModel.addDefaultLocation(defaultLocation)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Green)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
            Row() {
                Column {
                    Button(onClick = {
                        navController.navigate(Screen.SearchScreen.route)
                    }, Modifier.padding(top = 5.dp)) {
                        Text(text = "Add city", fontSize = 30.sp)
                    }
                }
                Spacer(Modifier.width(20.dp))
                Column {
                    Button(onClick = {
                        viewModel.clearLocations()
                    }, Modifier.padding(top = 5.dp)) {
                        Text(text = "Clear", fontSize = 30.sp)
                    }
                }
            }

            if (viewModel.weatherConditions.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp)
                ) {
                    Text(text = "No cities to display", color = Color.Black)

                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Cyan),
                    contentPadding = PaddingValues(all = 12.dp)
                ) {
                    items(viewModel.weatherConditions.size) { it ->
                        WeatherRow(weatherCondition = viewModel.weatherConditions[it])
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherRow(weatherCondition: WeatherCondition) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(Color.Cyan)
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${weatherCondition.weather[0].icon}@2x.png",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(2.dp)
                    .size(120.dp)
            )
            Text(text = weatherCondition.weather[0].main, color = Color.Black)
        }

        Column(Modifier.weight(1f)) {
            //Text(text = "45 \u2109")
            Text(
                text = weatherCondition.name,
                Modifier.padding(5.dp),
                textAlign = TextAlign.Start,
                fontSize = 24.sp,
                color = Color.Black
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${weatherCondition.main.temp.toInt()} \u2109",
                fontSize = 50.sp,
                color = Color.Black
            )
            Text(
                text = "Feels like ${weatherCondition.main.feels_like.toInt()} \u00B0",
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenComposable(rememberNavController(), null)
}
