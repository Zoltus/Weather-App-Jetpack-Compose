package fi.sulku.weatherapp.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class LocationService(private val app: Application) {
    private val geocoder = Geocoder(app)

    fun getCity(location: Location?): String? {
        return location?.let {
            getAddressFromLocation(it.latitude.toDouble(), it.longitude.toDouble())?.locality
        }
    }

    suspend fun getCurrentLocation(): Location? {
        return if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationProvider = LocationServices.getFusedLocationProviderClient(app)
            val location: android.location.Location = locationProvider.lastLocation.await()
            Location(location.latitude.toFloat(), location.longitude.toFloat())
        } else {
            Log.d("Location", "Not granted!")
            null
        }
    }

    fun getLocation(city: String): Location? {
        if (city.trim().isEmpty()) return null
        return getAddressFromCity(city)?.let { Location(it.latitude.toFloat(), it.longitude.toFloat()) }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): Address? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var address: Address? = null
            geocoder.getFromLocation(latitude, longitude, 1) { address = it.firstOrNull() }
            return address
        } else {
            @Suppress("deprecation")
            geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()
        }
    }

    private fun getAddressFromCity(city: String): Address? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var address: Address? = null
            geocoder.getFromLocationName(city, 1) { address = it.firstOrNull() }
            return address
        } else { //Older android
            @Suppress("deprecation")
            geocoder.getFromLocationName(city, 1)?.firstOrNull()
        }
    }
}