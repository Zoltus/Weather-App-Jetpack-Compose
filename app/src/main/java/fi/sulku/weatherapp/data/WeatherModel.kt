package fi.sulku.weatherapp.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val current: Current,
    val daily: Daily
)

@Serializable
data class Current(
    val time: String,
    val interval: Int,
    val temperature_2m: Double,
    val relative_humidity_2m: Int,
    val apparent_temperature: Double,
    val precipitation: Int,
    val weather_code: Int,
    val wind_speed_10m: Double
)

@Serializable
data class Daily(
    val time: List<String>,
    val weather_code: List<Int>,
    val temperature_2m_max: List<Double>,
    val temperature_2m_min: List<Double>,
    val sunrise: List<String>,
    val sunset: List<String>,
    val uv_index_max: List<Double>,
    val precipitation_sum: List<Double>,
    val precipitation_probability_max: List<Int>
)

//For hourly data:
@Serializable
data class HourlyResponse(
    val hourly: Hourly
)

@Serializable
data class Hourly(
    val time: List<String>,
    val temp2M: List<Double>,
    val apparentTemp: List<Double>,
    val weatherCode: List<Int>
)