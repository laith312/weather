package com.example.weather.ui.screen.search

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.weather.R
import com.example.weather.data.Location
import com.example.weather.ui.screen.Screen
import com.example.weather.ui.theme.WeatherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenComposable(navController: NavController) {
    WeatherTheme {
        val viewModel = viewModel<LocationSearchViewModel>()
        val focusManager = LocalFocusManager.current
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Cyan)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan)
                    .padding(bottom = 30.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.find_city),
                    fontSize = 36.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black
                    ),
                    value = viewModel.country,
                    maxLines = 1,
                    onValueChange = { viewModel.updateCountry(it) },
                    label = { Text(text = "Country Code") },
                    keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.background(Color.White)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black
                    ),
                    value = viewModel.state,
                    maxLines = 1,
                    onValueChange = { viewModel.updateState(it) },
                    label = { Text(text = "State") },
                    keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.background(Color.White)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black
                    ),
                    value = viewModel.city,
                    maxLines = 1,
                    onValueChange = { viewModel.updateCity(it) },
                    label = { Text(text = "City") },
                    keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.searchByCity()
                            focusManager.clearFocus()
                        }
                    ),
                    modifier = Modifier.background(Color.White)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(20.dp, 20.dp)
                )
                Text(text = "OR")
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .paddingFromBaseline(20.dp, 20.dp)
                )

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black
                    ),
                    value = viewModel.zipCode,
                    maxLines = 1,
                    onValueChange = { viewModel.updateZipcode(it) },
                    label = { Text(text = "zipcode") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.searchZipCode(viewModel.zipCode) }
                    ),
                    modifier = Modifier.background(Color.White)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Cyan)
            ) {
                items(viewModel.locationResult.size) { it ->
                    LocationRow(location = viewModel.locationResult[it]) {
                        viewModel.addLocation(viewModel.locationResult[it])
                        navController.popBackStack(Screen.MainScreen.route, inclusive = true)
                        navController.navigate(Screen.MainScreen.route)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationRow(location: Location, callback: () -> Unit?) {
    OutlinedButton(onClick = { callback.invoke() }, Modifier.background(Color.Gray)) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = location.name ?: "", fontSize = 24.sp)
            Row() {
                Text(text = location.state ?: "")
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = location.country ?: "")
            }
        }
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun LocationRowPreview() {
    LocationRow(Location("US", 0.0, 0.0, "city", "state", ""), {})
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenComposablePreview() {
    SearchScreenComposable(rememberNavController())
}