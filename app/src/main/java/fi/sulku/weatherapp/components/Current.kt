package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.data.Coordinates
import fi.sulku.weatherapp.data.WeatherViewModel
import fi.sulku.weatherapp.data.WeatherViewModel.weatherData
import java.util.Date

@Composable
fun Current() {
    val vm : WeatherViewModel = viewModel()
    val weatherData by vm.weatherData.collectAsState()

    val lahtiCoordinates = Coordinates(61.49911f, 23.78712f)
    val weather = weatherData[lahtiCoordinates]

    if (weather != null) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                //Left side items:
                Column(horizontalAlignment = Alignment.Start) {
                    Text("\uD83D\uDCCDLahti")
                    Text("Last update: ${Date(weather.lastUpdated)}")
                    Text(text = "<ConditionIcon>")
                    Text(text = weather.weather.current.temperature_2m.toString()) // Current temp
                    Text(text = "Weather Code: ${weather.weather.current.weather_code}")

                    Text("Sunny") // This should be replaced with actual weather condition
                    Text("↑${weather.weather.daily.temperature_2m_max[0]}℃ ↓${weather.weather.daily.temperature_2m_min[0]}℃")
                }
            }
            item {
                Column(horizontalAlignment = Alignment.End) {
                    Box(modifier = Modifier.height(100.dp)) //Empty space on the right
                    Text("☔ Rain Chance: ${weather.weather.daily.precipitation_probability_max[0]}%")
                    Text(text = "☃ Snow Chance: 0%") // This should be replaced with actual snow chance
                    Text(text = "\uD83D\uDD7A Feels Like: ${weather.weather.current.apparent_temperature}℃")
                }
            }
        }
    }
}