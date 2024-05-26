package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.SettingsRepository

/**
 * A component to display the weather details.
 *
 * Creates Vertical Grid with 2 columns to display the weather details.
 * Half on the left and half on the right side.
 *
 * @param weather The WeatherData to access the weather details.
 */
@Composable
fun Details(weather: WeatherData) {
    val context = LocalContext.current
    val locale by SettingsRepository.selectedLocale.collectAsState()
    val isMiles by SettingsRepository.isMiles.collectAsState()
    val isInches by SettingsRepository.isInches.collectAsState()
    // Convert rain amount to inches if needed
    val rain = weather.daily.rainAmount[1]
    val rainAmount = if (!isInches) "$rain mm" else String.format(locale, "%.2f", rain* 0.0393701) + " in"
    // Convert wind speed to mph if needed
    val wind = weather.current.windSpeed
    val windSpeed = if (!isMiles) "$wind m/s" else String.format(locale, "%.2f", wind* 2.23694) + " mph"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(stringResource(id = R.string.weather_details), fontWeight = FontWeight.Bold)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp)
            ) {
                item {
                    Column {
                        Text(stringResource(id = R.string.weather_rain_amount), fontWeight = FontWeight.Bold)
                        Text(rainAmount)
                        Text(stringResource(id = R.string.weather_humidity), fontWeight = FontWeight.Bold)
                        Text(weather.current.humidity.toString() + "%")
                        Text(stringResource(id = R.string.weather_uv), fontWeight = FontWeight.Bold)
                        Text(weather.daily.uv_index_max[1].toString())
                        Text(stringResource(id = R.string.weather_sunrise), fontWeight = FontWeight.Bold)
                        Text(weather.convertToClockTime(context, weather.daily.sunrise[1]))
                    }
                }
                item {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(stringResource(id = R.string.weather_wind_speed), fontWeight = FontWeight.Bold)
                        Text(windSpeed)
                        //Text("Visibility")
                        //Text("")
                        Text(stringResource(id = R.string.weather_air_pressure), fontWeight = FontWeight.Bold)
                        Text("${weather.current.pressure} hPa")
                        Text(stringResource(id = R.string.weather_sunset), fontWeight = FontWeight.Bold)
                        Text(weather.convertToClockTime(context, weather.daily.sunset[1]))
                    }
                }
            }
        }
    }
}