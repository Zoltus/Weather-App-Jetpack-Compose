package fi.sulku.weatherapp;

import android.content.Context
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle

object Utils {

    /**
     * Convert the given time to a clock time.
     * If the time is the same as the current hour, return "Now".
     * Otherwise, return the time in HH:mm format.
     *
     * @param time The time to convert.
     * @return The time in clock time format.
     */
    fun convertToClockTime(context: Context, time: String): String {
        val currentTime = LocalDateTime.now()
        val date = LocalDateTime.parse(time)
        val isSameHour = currentTime.hour == date.hour

        return if (isSameHour) {
            context.getString(R.string.weather_now)
        } else {
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            date.format(formatter)
        }
    }

    /**
     * Convert the given date to a day.
     *
     * If the date is today, return "Today".
     * Otherwise, return the day of the week in short format.
     *
     * @param context The context.
     * @param dateString The date to convert.
     * @return The date in day format.
     */
    fun convertDateToDay(context: Context, dateString: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, inputFormatter)
        return if (date == LocalDate.now()) {
            context.getString(R.string.weather_today)
        } else {
            date.dayOfWeek.getDisplayName(TextStyle.SHORT, SettingsRepository.selectedLocale.value)
        }
    }

    /**
     * Get the converted temperature.
     *
     * @param temp The temperature to convert.
     * @param isFahrenheit True if the temperature is in Fahrenheit.
     */
    fun getConvertedTempp(temp: Double, isFahrenheit: Boolean): Double {
        return if (isFahrenheit) {
            temp * 9 / 5 + 32
        } else {
            temp
        }
    }

    /**
     * Get the converted temperature.
     *
     * @param temp The temperature to convert.
     * @param isFahrenheit True if the temperature is in Fahrenheit.
     */
    fun getConvertedTemp(temp: Double, isFahrenheit: Boolean): String {
        return if (isFahrenheit) {
            "${String.format(SettingsRepository.selectedLocale.value, "%.1f", temp * 9 / 5 + 32)}°F" //Format
        } else {
            "$temp°C"
        }
    }
}
