package fi.sulku.weatherapp.components.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.platform.LocalContext
import fi.sulku.weatherapp.Utils
import fi.sulku.weatherapp.models.WeatherData

/**
 * Daily forecast component.
 *
 * Displays the daily forecast for the next 14 days.
 *
 * @param weather The WeatherData to access the daily weather information.
 * @param selectedDay The selected day index.
 */
@Composable
fun DailyCards(weather: WeatherData, selectedDay: MutableIntState) {
    val context = LocalContext.current
    List(weather.daily.maxTemps.size) {
        val iconCode = weather.getConditionIconId(weather.daily.weatherCode[it])
        val timeString = Utils.convertDateToDay(context, weather.daily.time[it])
        WeatherCard(
            time = timeString,
            temp = weather.daily.maxTemps[it],
            iconId = iconCode,
            onClick = { selectedDay.intValue = it }
        )
    }
}