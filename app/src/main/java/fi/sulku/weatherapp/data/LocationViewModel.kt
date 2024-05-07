package fi.sulku.weatherapp.data

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await


class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val app: Application = application

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

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
            val location: Location = locationProvider.lastLocation.await() // Get last location
            _location.value = location
        } else {
            Log.d("Location", "Not granted!")
        }
    }
}