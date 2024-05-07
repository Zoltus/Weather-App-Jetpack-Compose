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
import java.util.Date

@Composable
fun Current() {
    val vm : WeatherViewModel = viewModel()
    val weatherData by vm.weatherData.collectAsState()
    val currentLoc by vm.currentLocation.collectAsState()
    val weather = weatherData[currentLoc]

    if (weather != null) {
        val daily = weather.daily
        val current = weather.current

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
                    Text(text = "${current.temp}℃") // Current temp
                    Text(weather.getCurrentCondition())
                    Text("↑${daily.maxTemps[0]}℃ ↓${daily.minTemps[0]}℃")
                }
            }
            item {
                Column(horizontalAlignment = Alignment.End) {
                    Box(modifier = Modifier.height(100.dp))
                    Text("☔ Rain Chance: ${daily.rainChance[0]}%")
                    Text(text = "☃ Snow Chance: -11%")
                    Text(text = "\uD83D\uDD7A Feels Like: ${current.feelsLike}℃")
                }
            }
        }
    }
}