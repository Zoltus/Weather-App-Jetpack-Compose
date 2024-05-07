package fi.sulku.weatherapp.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
    private val _weatherCache =
        MutableStateFlow<Map<Location, WeatherData>>(emptyMap()) //todo sure has locs only min 500m apart
    private val _selectedLocation = MutableStateFlow<Location?>(null)

    //val selectedLocation: StateFlow<Location?> = _selectedLocation.asStateFlow()
    //Gets the weather data from the cache based on the selected location
    val weather: StateFlow<WeatherData?> = _selectedLocation
        .combine(_weatherCache) { loc, weatherCache -> weatherCache[loc] }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setWeather(loc: Location, data: WeatherData) {
        println("SetWerahter@@@@@@@@@@@")
        _selectedLocation.value = loc //Set selected location to new location
        //Copy map and set new stuff so its thread safe
        _weatherCache.value = _weatherCache.value.toMutableMap().also { it[loc] = data }
        println("Address is: ${getCity()}")
        //todo check if location is rounded?
    }

    fun getWeather(loc: Location): WeatherData? {
        return _weatherCache.value[loc]
    }

    fun getCity(): String? {
        val selectedLocation = _selectedLocation.value //fix casting
        if (selectedLocation != null) {
            val latitude = selectedLocation.latitude.toDouble()
            val longitude = selectedLocation.longitude.toDouble()
            val address: Address?
            val geocoder = Geocoder(app)
            address = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getNewAndroidGeo(geocoder, latitude, longitude)
            } else { //Older android
                getOldAndroidGeo(geocoder, latitude, longitude)
            }
            println("Address: ${address.toString()}")
            return address?.locality
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getNewAndroidGeo(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): Address? {
        var address: Address? = null
        geocoder.getFromLocation(latitude, longitude, 1) {
            address = it.firstOrNull()
        }
        return address
    }

    @Suppress("deprecation")
    private fun getOldAndroidGeo(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double
    ): Address? {
        return geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
    }


    /*
        @SuppressLint("MissingPermission")
    fun fetchLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location ?: return@addOnSuccessListener

            val geocoder = Geocoder(context, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= 33)
            {
                geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                    val address = addresses.firstOrNull()
                    val place = address?.locality ?: address?.subAdminArea ?: address?.adminArea
                    ?: address?.countryName
                    uiState = uiState.copy(place = place)
                }
            }
            else {
                val address =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                        ?: return@addOnSuccessListener
                val place =
                    address.locality ?: address.subAdminArea ?: address.adminArea ?: address.countryName
                    ?: return@addOnSuccessListener

                uiState = uiState.copy(place = place)
            }
        }
    }
     */
    /*
        private fun getAddressName(lat: Double, lon: Double): String? {
            var addressName: String? = null
            val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(lat, lon, 1) { list ->
                    if (list.size != 0) {
                        addressName = list[0].getAddressLine(0)
                        Log.d(TAG, "getAddressName: $addressName")
                        //show Marker
                    }
                }
            } else {
                try {
                    val list = geocoder.getFromLocation(lat, lon, 1)
                    if (list != null && list.size != 0) {
                        addressName = list[0].getAddressLine(0)
                        Log.d(TAG, "getAddressName: $addressName")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return addressName
        }
    */
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
            val location: LocationAndroid =
                locationProvider.lastLocation.await() // Get last location
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