package fi.sulku.weatherapp.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object WeatherViewModel : ViewModel() {

    // Cache for weather data todo some flowstate
    private val _weatherData = MutableStateFlow<Map<Location, WeatherData>>(emptyMap())
    val weatherData = _weatherData.asStateFlow()

    private val _selectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    //Copy map and set new stuff so its thread safe
    fun updateWeather(location: Location, data: WeatherData) {
        val currentData = _weatherData.value.toMutableMap()
        currentData[location] = data
        _weatherData.value = currentData
        _selectedLocation.value = location
    }

}