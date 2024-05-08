package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.data.WeatherViewModel
import kotlinx.coroutines.launch

/**
 * Search component.
 *
 * Allows user to get weather by his location. (User needs to allow location permissions)
 * If user denies the permission, a dialog will be shown to inform the user how to enable the permission.
 * User can also search by city name and then click the button to get the weather.
 *
 * @param weatherVm The WeatherViewModel to access the weather data.
 * @see GpsLocButton
 */
@Composable
fun Search(weatherVm: WeatherViewModel) {
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    Text(text = "Check Weather")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Row {
            GpsLocButton()
            Spacer(modifier = Modifier.width(8.dp))
            TextField(modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
                shape = RoundedCornerShape(20),
                value = input, onValueChange = { input = it },
                placeholder = { Text("City name") }
            )
        }

        Button(modifier = Modifier.fillMaxWidth()
            .padding(top = 16.dp),
            shape = RoundedCornerShape(20),
            onClick = {
                scope.launch { vm.fetchWeather(input) }
            }) {
            Text("Get Weather")
        }
    }

}

