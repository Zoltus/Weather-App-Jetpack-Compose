package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.Utils.getConvertedTemp
import fi.sulku.weatherapp.viewmodels.SettingsRepository

/**
 * A Card to display the hourly weather information.
 *
 * @param time The time for current hour.
 * @param temp The temperature of the hour.
 * @param iconId The icon of the weather condition.
 */
@Composable
fun WeatherCard(time: String, temp: Double, iconId: Int, onClick: () -> Unit = {}) {
    val isFahrenheit by SettingsRepository.isFahrenheit.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onClick() }
    ) {
        Text(time)
        Image(
            modifier = Modifier.size(52.dp),
            painter = painterResource(id = iconId),
            contentDescription = "Weather icon"
        )
        Text(getConvertedTemp(temp, isFahrenheit))
    }
}