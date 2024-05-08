package fi.sulku.weatherapp.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for weather data.
 *
 * Handles the weather data and location data.
 *
 * @param application Application context.
 */
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

    /**
     * Set the weather data to the cache.
     * If the location is already in the cache, update the data.
     *
     * @param loc Location of the weather data.
     * @param data Weather data to be set.
     */
    private fun setWeather(loc: Location, data: WeatherData) {
        println("SetWerahter@@@@@@@@@@@")
        _selectedLocation.value = loc //Set selected location to new location
        _weatherCache.value = _weatherCache.value.toMutableMap().also { it[loc] = data }
    }


    /**
     * Get the city name from the selected location.
     *
     * @return City name of the selected location.
     */
    fun getCity(): String? {
        return locationService.getCity(_selectedLocation.value)
    }

    /**
     * Update the weather data for the given city.
     */
    suspend fun selectCity(city: String) {
        val location: Location? = locationService.getLocation(city)
        checkWeatherUpdates(location)
    }

    /**
     * Update the weather data for the current location.
     */
    suspend fun selectCurrentCity() {
        checkWeatherUpdates(locationService.getCurrentLocation())
    }

    /**
     * Check if weatherdata is up to date for given location.
     * If the weather data is already in the cache and it is up to date do nothing.
     * If its out of date or not in the cache, fetch the new data
     * and set it to the cache and variables
     *
     * @param loc Location to fetch the weather data for.
     */
    private suspend fun checkWeatherUpdates(loc: Location?) {
        if (loc == null) return
        val weatherData: WeatherData? = _weatherCache.value[loc]
        if (weatherData != null && !weatherData.needsUpdate()) {
            setWeather(loc, weatherData)
        } else {
            val newWeather = weatherApiService.fetchWeather(loc)
            setWeather(loc, newWeather)
        }
    }
}