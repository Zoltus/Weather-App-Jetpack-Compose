package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.viewmodels.WeatherViewModel

/**
 * Weather section
 *
 * Displays all weather related stuff
 *
 * @param weatherVm The WeatherViewModel to access the weather information.
 */
@Composable
fun WeatherSection(weatherVm: WeatherViewModel) {
    val selectedWeather by weatherVm.selectedWeather.collectAsState()

    //If no weather data is selected, show loading text
    if (selectedWeather == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading Weather Data...")
        }
    }

    selectedWeather?.let { weather ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)) //Bottom rounded corners
        ) {
            item { Current(weatherVm, weather) }
            item { Spacer(modifier = Modifier.padding(10.dp)) }
            item { Hourly(weatherVm, weather) }
            item { Daily(weatherVm, weather) }
            item { Spacer(modifier = Modifier.padding(10.dp)) }
            item { Details(weatherVm, weather.daily) }
        }
    }
}