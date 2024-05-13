package fi.sulku.weatherapp.services

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import fi.sulku.weatherapp.models.Location
import kotlinx.coroutines.tasks.await
import java.util.Locale

/**
 * Service for handling location operations.
 *
 * @param app Application context.
 */
object LocationService {
    private lateinit var app: Application
    private lateinit var geocoder: Geocoder

    fun initialize(application: Application) {
        app = application
        geocoder = Geocoder(app)
    }
    fun setLocale(locale: Locale) {
        geocoder = Geocoder(app, locale)
    }

    /**
     * Get the current location.
     *
     * Ask for location permissions.
     * If the permissions are granted, get the current location.
     *
     * @return Location object of the current location.
     */
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

    /**
     * Get the city name from the location.
     *
     * @param location Location to get the city name from.
     * @return City name of the location.
     */
    fun getCity(location: Location?): String? {
        return location?.let {
            val address = getAddressFromLocation(it.latitude.toDouble(), it.longitude.toDouble())
            val city = address?.locality
            val country = city ?: address?.countryName
            return country
        }
    }

    /**
     * Get the location from the city name.
     *
     * @param city City name to get the location from.
     * @return Location object of the city.
     */
    fun getLocation(city: String): Location? {
        if (city.trim().isEmpty()) return null
        return getAddressFromCity(city)?.let { Location(it.latitude.toFloat(), it.longitude.toFloat()) }
    }

    /**
     * Get the address from latitude and longitude.
     * Gets address differently based on the android version.
     *
     * @param latitude Latitude of the location.
     * @param longitude Longitude of the location.
     * @return Address object of the location.
     */
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

    /**
     * Get the address from city name.
     * Gets address differently based on the android version.
     *
     * @param city City name to get the location from.
     * @return Address object of the location.
     */
    private fun getAddressFromCity(city: String): Address? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            var address: Address? = null
            geocoder.getFromLocationName(city, 1) { address = it.firstOrNull() }
            return address
        } else { //Older android
           geocoder.getFromLocationName(city, 1)?.firstOrNull()
        }
    }
}