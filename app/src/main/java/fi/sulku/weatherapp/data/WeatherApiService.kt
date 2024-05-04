package fi.sulku.weatherapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object WeatherApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    // Cache for weather data
    private val weatherData = mutableMapOf<Coordinates, WeatherData>()

    suspend fun fetchWeather(coords: Coordinates): WeatherData {
        val weatherData = weatherData[coords]
        return if (weatherData != null) {
            weatherData
        } else {
            val latitude = coords.latitude
            val longitude = coords.longitude
            val weatherUrl =
                "https://api.open-meteo.com/v1/forecast?$latitude&longitude=$longitude&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_sum,precipitation_probability_max&timezone=auto&forecast_days=14"
            val weather: WeatherResponse = client.get(weatherUrl).body()
            val hourlyUrl =
                "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,apparent_temperature,weather_code&timezone=auto&forecast_days=3"
            val hourly: HourlyResponse = client.get(hourlyUrl).body()
            WeatherData(weather, hourly)
        }
    }
}

data class Coordinates(val latitude: Double, val longitude: Double)

data class WeatherData(val weather: WeatherResponse, val hourly: HourlyResponse)