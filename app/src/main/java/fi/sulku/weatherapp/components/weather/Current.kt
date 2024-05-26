package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import fi.sulku.weatherapp.viewmodels.SettingsRepository.getConvertedTemp
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
    val isFahrenheit by SettingsRepository.isFahrenheit.collectAsState()

    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column { //Left side
                Text("\uD83D\uDCCD${vm.getCity()}", fontWeight = FontWeight.Bold) //todo change getcity method
                Text("${stringResource(R.string.weather_last_updated)}: ${weather.getLastUpdated()}")
                Spacer(modifier = Modifier.height(20.dp))
                Image( modifier = Modifier.size(96.dp),
                    painter = painterResource(weather.getConditionIconId()),
                    contentDescription = "Weather icon"
                )
                Text(
                    text = getConvertedTemp(current.temp, isFahrenheit),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                ) // Current temp
                Spacer(modifier = Modifier.height(20.dp))
                Text(weather.getCurrentCondition(context), fontWeight = FontWeight.Bold)
                Text(
                    "↑${
                        getConvertedTemp(
                            daily.maxTemps[0],
                            isFahrenheit
                        )
                    } ↓${getConvertedTemp(daily.minTemps[0], isFahrenheit)}"
                )
            }
            Column {
                Box(modifier = Modifier.height(120.dp))
                Text("☔ ${stringResource(R.string.weather_rain_chance)}: ${daily.rainChance[0]}%")
                //Text(text = "☃ Snow Chance: -11%")
                Text(
                    text = "\uD83D\uDD7A ${stringResource(R.string.weather_feels_like)}: ${
                        getConvertedTemp(current.feelsLike, isFahrenheit)
                    }"
                )
            }
        }
    }
}
