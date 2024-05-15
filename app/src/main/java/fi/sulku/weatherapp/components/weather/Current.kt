package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.weatherapp.R
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
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            //Left side items:
            Column {
                Text("\uD83D\uDCCD${vm.getCity()}", fontWeight = FontWeight.Bold) //todo change getcity method
                Text("${stringResource(R.string.weather_last_updated)}: ${weather.getLastUpdated()}")
                Spacer(modifier = Modifier.height(20.dp))
                //Text(text = "<ConditionIcon>")
                Text(text = "${current.temp}℃", fontWeight = FontWeight.Bold, fontSize = 32.sp) // Current temp
                Spacer(modifier = Modifier.height(20.dp))
                Text(weather.getCurrentCondition(context), fontWeight = FontWeight.Bold)
                Text("↑${daily.maxTemps[0]}℃ ↓${daily.minTemps[0]} ℃")
            }
        }
        item {
            Column {
                Box(modifier = Modifier.height(50.dp))
                Text("☔ ${stringResource(R.string.weather_rain_chance)}: ${daily.rainChance[0]}%")
                //Text(text = "☃ Snow Chance: -11%")
                Text(text = "\uD83D\uDD7A ${stringResource(R.string.weather_feels_like)}: ${current.feelsLike}℃")
            }
        }
    }
}