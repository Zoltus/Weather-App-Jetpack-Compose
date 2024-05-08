package fi.sulku.weatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class WeatherData(
    val latitude: Float,
    val longitude: Float, //todo ?
    val daily: Daily,
    val current: Current,
    val hourly: Hourly,
) {
    // Checks if 15min is passed since last update
    fun needsUpdate(): Boolean {
        val date = LocalDateTime.parse(current.time).plusMinutes(15)
        val now: LocalDateTime = LocalDateTime.now()
        return now.isAfter(date)
    }

    fun getLastUpdated(): String {
        val date = LocalDateTime.parse(current.time)
        return date.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun getCurrentCondition(): String {
        return getCondition(current.weather_code)
    }

    fun getCondition(weatherCode: Int): String {
        return when (weatherCode) {
            0 -> "Selkeää"
            in 1..3 -> "Puolipilvistä"
            in 45..48 -> "Sumua"
            in 51..55 -> "Tihkusadetta"
            in 56..57 -> "Jäätävää tihkua"
            in 61..65 -> "Sadetta"
            in 66..67 -> "Jäätävää sadetta"
            in 71..75 -> "Lumisadetta"
            77 -> "Lumirakeita"
            in 80..82 -> "Sadekuuroja"
            in 85..86 -> "Lumikuuroja"
            in 95..96 -> "Ukkosta"
            99 -> "Voimakasta ukkosta"
            else -> "Tuntematon"
        }
    }
}

@Serializable
data class Current(
    val time: String,
    @SerialName("temperature_2m")
    val temp: Double,
    @SerialName("relative_humidity_2m")
    val humidity: Int,
    @SerialName("apparent_temperature")
    val feelsLike: Double,
    val precipitation: Double,
    val weather_code: Int,
    @SerialName("wind_speed_10m")
    val windSpeed: Double
)

@Serializable
data class Daily(
    val time: List<String>,
    val weather_code: List<Int>,
    @SerialName("temperature_2m_max")
    val maxTemps: List<Double>,
    @SerialName("temperature_2m_min")
    val minTemps: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val uv_index_max: List<Double>,
    @SerialName("precipitation_sum")
    val rainAmount: List<Double>,
    @SerialName("precipitation_probability_max") //Todo if its right
    val rainChance: List<Int>
)

@Serializable
data class Hourly(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temps: List<Double>,
    @SerialName("apparent_temperature")
    val feelsLike: List<Double>,
    val weather_code: List<Int>
)