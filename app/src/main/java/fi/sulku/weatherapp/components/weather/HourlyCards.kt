package fi.sulku.weatherapp.components.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import fi.sulku.weatherapp.Utils
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.models.getConditionIconId
import java.time.LocalDateTime

/**
 * Create hourly cards for the hourly forecast.
 *
 * Filters the hourly weather information to display only the next 24 hours
 * and then creates a HourlyCard for each hour.
 *
 * @param weather The WeatherData to access the hourly weather information.
 * @see WeatherCard
 */
@Composable
fun HourlyCards(weather: WeatherData, dayIndex: Int) {
    val context = LocalContext.current
    var currentTime = LocalDateTime.now().minusHours(1).plusDays(dayIndex.toLong() - 1)
    val nextDayTime = currentTime.plusDays(1)
    if (dayIndex != 1) {
        currentTime = currentTime.withHour(0).withMinute(0).withSecond(0)
    }

    List(weather.hourly.temps.size) {
        val time = LocalDateTime.parse(weather.hourly.time[it])
        val iconCode = getConditionIconId(weather.hourly.weatherCode[it])
        //start from current hours and end 24h after:
        if (time.isAfter(currentTime) && time.isBefore(nextDayTime)) {
            val timeString = Utils.convertToClockTime(context, weather.hourly.time[it])
            WeatherCard(
                time = timeString,
                temp = weather.hourly.temps[it],
                iconId = iconCode
            )
        }
    }
}