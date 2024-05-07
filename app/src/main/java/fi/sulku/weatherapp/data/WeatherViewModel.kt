package fi.sulku.weatherapp.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import android.location.Location as LocationAndroid

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    //private val _isFetching = MutableStateFlow(false)
    //val isFetching: StateFlow<Boolean> = _isFetching.asStateFlow()
    private val app: Application = application
    private val _weatherCache = MutableStateFlow<Map<Location, WeatherData>>(emptyMap())
    private val _selectedLocation = MutableStateFlow<Location?>(null)
    //val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()
    //Gets the weather data from the cache based on the selected location
    val weather: StateFlow<WeatherData?> = _selectedLocation
        .combine(_weatherCache) { loc, weatherCache -> weatherCache[loc] }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setWeather(loc: Location, data: WeatherData) {
        _selectedLocation.value = loc //Set selected location to new location
        //Copy map and set new stuff so its thread safe
        _weatherCache.value = _weatherCache.value.toMutableMap().also { it[loc] = data}
    }

    fun getWeather(loc: Location): WeatherData? {
        return _weatherCache.value[loc]
    }

    suspend fun setCurrentLocation() {
        // Check if location permissions are given
        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationProvider = LocationServices.getFusedLocationProviderClient(app)
            val location: LocationAndroid = locationProvider.lastLocation.await() // Get last location
            val fLocation = Location(location.latitude.toFloat(), location.longitude.toFloat())
            WeatherApiService.updateWeather(this, fLocation, viewModelScope)
        } else {
            Log.d("Location", "Not granted!")
        }
    }
}

//getFromLocationName
// val fromLocation: Unit =
//  Geocoder(app).getFromLocation(location.latitude, location.longitude, 1) {
// }
//location.distanceTo(android.location.Location("Tampere"))