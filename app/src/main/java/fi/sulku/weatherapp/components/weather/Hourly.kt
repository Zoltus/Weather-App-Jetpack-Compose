package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import java.time.LocalDateTime
import java.time.format.TextStyle

/**
 * Horizontal scrollable hourly forecast component.
 *
 * Displays the hourly forecast for the next 24 hours.
 *
 * @param weather The WeatherData to access the hourly weather information.
 * @see HourlyCards
 */
@Composable
fun Hourly(weather: WeatherData, selectedDayIndex: Int) {
    val locale by SettingsRepository.selectedLocale.collectAsState()
    val now = LocalDateTime.now()
    val adjustedDay = now.plusDays((selectedDayIndex - 1).toLong())
    val day = if (adjustedDay == now) stringResource(id = R.string.weather_today) else adjustedDay.dayOfWeek.getDisplayName(TextStyle.FULL, locale)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                stringResource(id = R.string.weather_hourly_forecast) + " ($day)",
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                HourlyCards(weather, selectedDayIndex)
            }
        }
    }
}

