package fi.sulku.weatherapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.weatherapp.models.Location
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.services.LocationService
import fi.sulku.weatherapp.services.WeatherApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

/**
 * ViewModel for weather data.
 *
 * Handles the weather data and location data.
 *
 */
class WeatherViewModel : ViewModel() {
    private val weatherApiService = WeatherApiService()
    private val _isLoading = MutableStateFlow(false)
    private val _selectedLocation = MutableStateFlow(SettingsRepository.lastSelectedLocation.value)
    private val _weatherCache = MutableStateFlow<Map<Location, WeatherData>>(emptyMap())

    val selectedLocation = _selectedLocation.asStateFlow()
    val isLoading = _isLoading.asStateFlow()
    //Gets the weather data from the cache based on the selected location
    val selectedWeather: StateFlow<WeatherData?> = _selectedLocation
        .combine(_weatherCache) { loc, weatherCache -> weatherCache[loc] }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Set the weather data to the cache.
     * If the location is already in the cache, update the data.
     * Also used in glancewidget to set old weater data.
     *
     * @param loc Location of the weather data.
     * @param data Weather data to be set.
     */
    private fun setWeather(loc: Location, data: WeatherData) {
        Timber.d("Setting weather to $loc")
        _selectedLocation.value = loc
        _weatherCache.value = _weatherCache.value.toMutableMap().also { it[loc] = data }
        SettingsRepository.setLastLocation(loc)
        SettingsRepository.setLastWeather(data)
    }

    /**
     * Get the city name from the selected location.
     *
     * @return City name of the selected location.
     */
    fun getCity(): String? {
        return LocationService.getCity(_selectedLocation.value)
    }


    /**
     * Select the city and fetch the weather data.
     * If the weather data is already in the cache and it is up to date do nothing.
     *
     * @param city City to fetch the weather data for.
     */
    suspend fun selectCity(city: String) : WeatherData? {
        val location: Location? = LocationService.getLocation(city)
        return checkWeatherUpdates(location)
    }

    /**
     * Select the location and fetch the weather data.
     *
     * If the weather data is already in the cache and it is up to date do nothing.
     *
     * @param latitude Latitude of the location.
     */
    suspend fun selectLocation(latitude: Double, longitude: Double) : WeatherData? {
        return checkWeatherUpdates(Location(latitude, longitude))
    }

    /**
     * Update the weather data for the current location.
     */
    suspend fun selectCurrentCity() : WeatherData? {
        return checkWeatherUpdates(LocationService.getCurrentLocation())
    }

    /**
     * Check if weatherdata is up to date for given location.
     * If the weather data is already in the cache and it is up to date do nothing.
     * If its out of date or not in the cache, fetch the new data
     * and set it to the cache and variables
     *
     * @param loc Location to fetch the weather data for.
     */
    private suspend fun checkWeatherUpdates(loc: Location?) : WeatherData? {
        Timber.d("Checking weather for $loc")
        var weatherData: WeatherData?
        if (loc == null) return null
        _isLoading.value = true
        weatherData = _weatherCache.value[loc]
        // Up to date
        if (weatherData != null && !weatherData.needsUpdate()) {
            Timber.d("Weather is up to date for $loc")
            setWeather(loc, weatherData)
        } else { // Updates weather data
            Timber.d("Fetching new weather for $loc")
            weatherData = weatherApiService.fetchWeather(loc)
            setWeather(loc, weatherData)
            Timber.d("Weather fetched for $loc")
        }
        _isLoading.value = false
        return weatherData
    }
}