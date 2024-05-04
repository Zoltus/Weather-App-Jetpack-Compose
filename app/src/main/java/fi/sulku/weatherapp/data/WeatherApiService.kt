package fi.sulku.weatherapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

object WeatherApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getWeather(coords: Coordinates, scope: CoroutineScope): WeatherData {
        println("Getting weather data for $coords")
        val weatherData = WeatherViewModel.weatherData.value[coords]
        // If weatherdata exist or doesnt need update return it
        if (weatherData != null && !weatherData.needsUpdate()) {
            return weatherData
        }
        // Updates/gets new weather data and updates viewmodel
        return fetchWeather(coords, scope).also {
            WeatherViewModel.updateWeather(coords, it)
        }
    }

    private suspend fun fetchWeather(coords: Coordinates, scope: CoroutineScope): WeatherData {
        println("Fetching weather data for $coords")
        val latitude = coords.latitude
        val longitude = coords.longitude

        val weatherUrl =
            "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_sum,precipitation_probability_max&timezone=auto&forecast_days=14"
        val hourlyUrl =
            "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,apparent_temperature,weather_code&timezone=auto&forecast_days=3"
        val response: HttpResponse = client.get(weatherUrl)
        println(response)
        println("Data: " + response.bodyAsText())

        val weatherAsync = scope.async { client.get(weatherUrl).body<WeatherResponse>() }
        val hourlyAsync = scope.async { client.get(hourlyUrl).body<HourlyResponse>() }

        val weather: WeatherResponse = weatherAsync.await()
        val hourly: HourlyResponse = hourlyAsync.await()
        return WeatherData(weather, hourly, System.currentTimeMillis())
    }
}

data class Coordinates(val latitude: Float, val longitude: Float)

data class WeatherData(
    val weather: WeatherResponse,
    val hourly: HourlyResponse,
    val lastUpdated: Long
) {
    fun needsUpdate(): Boolean {
        return System.currentTimeMillis() - lastUpdated > 5 * 60 * 1000 //10min
    }


    // Override and equals methods to iqnote LastUpdated time
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WeatherData) return false
        if (weather != other.weather) return false
        if (hourly != other.hourly) return false
        return true
    }

    override fun hashCode(): Int {
        var result = weather.hashCode()
        result = 31 * result + hourly.hashCode()
        return result
    }
}