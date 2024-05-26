package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.viewmodels.SettingsRepository

/**
 * A Card to display the hourly weather information.
 *
 * @param time The time for current hour.
 * @param temp The temperature of the hour.
 * @param icon The icon of the weather condition.
 */
@Composable
fun HourlyCard(time: String, temp: Double, icon: Int) {
    // Padding:
    val isFahrenheit by SettingsRepository.isFahrenheit.collectAsState()
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(time)
        Text("☀️")//${icon}
        Text(SettingsRepository.getConvertedTemp(temp, isFahrenheit))
    }
}