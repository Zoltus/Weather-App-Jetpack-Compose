package fi.sulku.weatherapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //private val _isFetching = MutableStateFlow(false)
    //val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()
    //val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()
    private val app: Application = application
    private val locationService: LocationService = LocationService(app)
    private val weatherApiService: WeatherApiService = WeatherApiService()
    private val _selectedLocation = MutableStateFlow<Location?>(null)

    //todo sure has locs only min 500m apart
    private val _weatherCache = MutableStateFlow<Map<Location, WeatherData>>(emptyMap())

    //Gets the weather data from the cache based on the selected location
    val selectedWeather: StateFlow<WeatherData?> = _selectedLocation
        .combine(_weatherCache) { loc, weatherCache -> weatherCache[loc] }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    //todo check if location is rounded?
    //Copy map and set new stuff so its thread safe
    private fun setWeather(loc: Location, data: WeatherData) {
        println("SetWerahter@@@@@@@@@@@")
        _selectedLocation.value = loc //Set selected location to new location
        _weatherCache.value = _weatherCache.value.toMutableMap().also { it[loc] = data }
    }

    fun getCity(): String? {
        return locationService.getCity(_selectedLocation.value)
    }

    suspend fun fetchWeather(city: String): WeatherData? {
        val location: Location? = locationService.getLocation(city)
        return getOrUpdateWeather(location)
    }

    suspend fun fetchWeather(): WeatherData? {
        return getOrUpdateWeather(locationService.getCurrentLocation())
    }

    private suspend fun getOrUpdateWeather(loc: Location?): WeatherData? {
        if (loc == null) return null;
        val weatherData: WeatherData? = _weatherCache.value[loc]
        if (weatherData != null && !weatherData.needsUpdate()) {
            return weatherData
        } else {
            val newWeather = weatherApiService.fetchWeather(loc)
            setWeather(loc, newWeather)
            return newWeather
        }
    }
}