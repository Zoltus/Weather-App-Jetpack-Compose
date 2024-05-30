package fi.sulku.weatherapp.components.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.Utils.convertToClockTime
import fi.sulku.weatherapp.models.Daily
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import java.time.LocalDateTime
import java.time.format.TextStyle

/**
 * A component to display the weather details.
 *
 * Creates Vertical Grid with 2 columns to display the weather details.
 * Half on the left and half on the right side.
 *
 * @param daily The Daily to access the daily weather information.
 * @param selectedDay The selected day index.
 */
@Composable
fun Details(daily: Daily, selectedDay : Int) {
    val context = LocalContext.current
    val locale by SettingsRepository.selectedLocale.collectAsState()
    val isMiles by SettingsRepository.isMiles.collectAsState()
    val isInches by SettingsRepository.isInches.collectAsState()
    // Convert rain amount to inches if needed
    val rain = daily.rainAmount[selectedDay]
    val rainAmount = if (!isInches) "$rain mm" else String.format(locale, "%.2f", rain* 0.0393701) + " in"
    // Convert wind speed to mph if needed
    val wind = daily.windSpeed[selectedDay]
    val windSpeed = if (!isMiles) "$wind m/s" else String.format(locale, "%.2f", wind* 2.23694) + " mph"

    val now = LocalDateTime.now()
    val adjustedDay = now.plusDays((selectedDay - 1).toLong())
    val day = if (adjustedDay == now) stringResource(id = R.string.weather_today) else adjustedDay.dayOfWeek.getDisplayName(TextStyle.FULL, locale)

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

            Text(stringResource(id = R.string.weather_details) + " ($day)", fontWeight = FontWeight.Bold)
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(id = R.string.weather_rain_amount), fontWeight = FontWeight.Bold)
                        Text(rainAmount)
                        Text(stringResource(id = R.string.weather_uv), fontWeight = FontWeight.Bold)
                        Text(daily.uvIndex[selectedDay].toString())
                        Text(stringResource(id = R.string.weather_sunrise), fontWeight = FontWeight.Bold)
                        Text(convertToClockTime(context, daily.sunrise[selectedDay]))
                    }
                    Column {
                        Text(stringResource(id = R.string.weather_wind_speed), fontWeight = FontWeight.Bold)
                        Text(windSpeed)
                        Text(stringResource(id = R.string.weather_sunset), fontWeight = FontWeight.Bold)
                        Text(convertToClockTime(context, daily.sunset[selectedDay]))
                    }
                }
            }
        }
    }
}