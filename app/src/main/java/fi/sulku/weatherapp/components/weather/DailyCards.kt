package fi.sulku.weatherapp.components.weather

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import fi.sulku.weatherapp.Utils
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.models.getConditionIconId
import fi.sulku.weatherapp.viewmodels.WeatherViewModel

/**
 * Daily forecast component.
 *
 * Displays the daily forecast for the next 14 days.
 *
 * @param vm ViewModel to select the day.
 * @param weather The WeatherData to access the daily weather information.
 */
@Composable
fun DailyCards(vm: WeatherViewModel, weather: WeatherData) {
    val context = LocalContext.current
    List(weather.daily.maxTemps.size) {
        val iconCode = getConditionIconId(weather.daily.weatherCode[it])
        val timeString = Utils.convertDateToDay(context, weather.daily.time[it])
        WeatherCard(
            time = timeString,
            temp = weather.daily.maxTemps[it],
            iconId = iconCode,
            onClick = { vm.selectDay(it) }
        )
    }
}