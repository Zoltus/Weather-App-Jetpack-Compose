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
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.models.WeatherData

/**
 * Daily forecast component.
 * Displays the daily forecast for the next 14 days.
 *
 * @param weather The WeatherData to access the daily weather information.
 * @param selectedDay The selected day index.
 */
@Composable
fun Daily(weather: WeatherData, selectedDay: MutableIntState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                stringResource(id = R.string.weather_daily_forecast) + "",
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DailyCards(weather, selectedDay)
                }
                WeatherChart(weather)
            }
        }
    }
}

