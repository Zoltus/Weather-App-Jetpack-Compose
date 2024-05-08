package fi.sulku.weatherapp.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json
import kotlin.math.round

object WeatherApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun updateWeather(viewModel: WeatherViewModel, location: Location, scope: CoroutineScope): WeatherData {
        println("Getting weather data for $location")
        val weatherData = viewModel.getWeather(location)
        // If weatherdata exist or doesnt need update return it
        if (weatherData != null && !weatherData.needsUpdate()) {
            return weatherData
        }
        // Updates/gets new weather data and updates viewmodel
        return fetchWeather(location, scope).also {
            viewModel.setWeather(location, it)
        }
    }

    private suspend fun fetchWeather(location: Location, scope: CoroutineScope): WeatherData {
        println("Fetching weather data for $location")
        val url = createUrl(location)
        val weatherAsync = scope.async { client.get(url).body<WeatherData>() }
        val weather: WeatherData = weatherAsync.await()
        return WeatherData(weather.daily, weather.current, weather.hourly)
    }

    private fun createUrl(location: Location) : String {
        return "https://api.open-meteo.com/v1/forecast?latitude=${location.latitude}&longitude=${location.longitude}&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m&hourly=temperature_2m,apparent_temperature,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_max,precipitation_sum,precipitation_probability_max&timezone=Europe%2FLondon&past_days=1&forecast_days=14"
    }
}

data class Location(var latitude: Float, var longitude: Float) {
    init {
        latitude = roundtoClosest500m(latitude)
        longitude = roundtoClosest500m(longitude)
    }
    //Round to closest 500m to save cache and requests
    private fun roundtoClosest500m(coordinate: Float): Float {
        val coordinateInMeters = coordinate * 111139 // Convert to meters
        val roundedCoordinate = round(coordinateInMeters / 500) * 500 // Round to nearest 500 meters
        return roundedCoordinate / 111139 // Convert back to degrees
    }
}

