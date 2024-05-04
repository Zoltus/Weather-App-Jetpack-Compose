package fi.sulku.weatherapp.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object WeatherViewModel : ViewModel() {

    // Cache for weather data todo some flowstate
    private val _weatherData = MutableStateFlow<Map<Coordinates, WeatherData>>(emptyMap())

    val weatherData = _weatherData.asStateFlow()

    //Copy map and set new stuff so its thread safe
    fun updateWeather(coords: Coordinates, data: WeatherData) {
        val currentData = _weatherData.value.toMutableMap()
        currentData[coords] = data
        _weatherData.value = currentData
    }

}