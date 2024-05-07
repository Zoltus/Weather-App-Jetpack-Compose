package fi.sulku.weatherapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val current: Current,
    val daily: Daily
)
//todo with 1 request, click day to change details, bookmark citys?

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

//For hourly data:
@Serializable
data class HourlyResponse(
    val hourly: Hourly
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