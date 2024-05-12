package fi.sulku.weatherapp.services

import fi.sulku.weatherapp.models.Location
import fi.sulku.weatherapp.models.WeatherData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Service for fetching weather data from the Open-Meteo API.
 *
 * Fetches the weather data for the given location
 * And creates a WeatherData object from the response using Ktor client.
 *
 * @see WeatherData
 */
class WeatherApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Fetch the weather data for the given location.
     *
     * @param location The location to fetch the weather data for.
     * @return WeatherData object containing the weather data.
     */
    suspend fun fetchWeather(location: Location): WeatherData {
        val url = createUrl(location)
        return client.get(url).body<WeatherData>()
    }

    /**
     * Create the URL for the Open-Meteo API request.
     *
     * @param location The location to create the URL for.
     */
    private fun createUrl(location: Location): String {
        return "https://api.open-meteo.com/v1/forecast?latitude=${location.latitude}&longitude=${location.longitude}&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,surface_pressure,wind_speed_10m&hourly=temperature_2m,apparent_temperature,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_sum,precipitation_probability_max&timezone=auto&past_days=1&forecast_days=14"
     }
}