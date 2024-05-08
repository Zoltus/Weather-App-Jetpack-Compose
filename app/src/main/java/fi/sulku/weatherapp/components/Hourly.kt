package fi.sulku.weatherapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.data.WeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Hourly(weather: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFBFDBFE),
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Text("HOURLY FORECAST")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            createHourlyCards(weather)
        }
    }
}

@Composable
fun createHourlyCards(weather: WeatherData) {
    val currentTime = LocalDateTime.now().minusHours(1)
    val nextDayTime = currentTime.plusDays(1)
    List(weather.hourly.temps.size) {
        val time = LocalDateTime.parse(weather.hourly.time[it])
        //start from current hours and end 24h after:
        if (time.isAfter(currentTime) && time.isBefore(nextDayTime)) {
            println("Time: " + weather.hourly.time[it])
            val timeString = getDateAsClockTime(weather.hourly.time[it])
            HourlyCard(
                time = timeString,
                temp = weather.hourly.temps[it],
                icon = weather.hourly.weather_code[it]
            )
        }
    }
}

private fun getDateAsClockTime(time: String): String {
    val currentTime = LocalDateTime.now()
    val date = LocalDateTime.parse(time)
    println("IsSameHour: " + currentTime.hour + " Other: " + date.hour)
    val isSameHour = currentTime.hour == date.hour
    return if (isSameHour) {
        "Now"
    } else {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        date.format(formatter)
    }
}