package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.WeatherViewModel

/**
 * A component to display the current weather information.
 *
 * Creates Vertical Grid with 2 columns to display the current weather basic information.
 * Half on the left and half on the right side.
 *
 * @param vm ViewModel to access the city name.
 * @param weather The WeatherData to access the current weather information.
 */
@Composable
fun Current(vm: WeatherViewModel, weather: WeatherData) {
    val daily = weather.daily
    val current = weather.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            //Left side items:
            Column {
                Text("\uD83D\uDCCD${vm.getCity()}")
                Text("Last update: ${weather.getLastUpdated()}")
                //Text(text = "<ConditionIcon>")
                Text(text = "${current.temp}℃") // Current temp
                Text(weather.getCurrentCondition())
                Text("↑${daily.maxTemps[0]}℃ ↓${daily.minTemps[0]}℃")
            }
        }
        item {
            Column(horizontalAlignment = Alignment.End) {
                Box(modifier = Modifier.height(100.dp))
                Text("☔ Rain Chance: ${daily.rainChance[0]}%")
                //Text(text = "☃ Snow Chance: -11%")
                Text(text = "\uD83D\uDD7A Feels Like: ${current.feelsLike}℃")
            }
        }
    }
}