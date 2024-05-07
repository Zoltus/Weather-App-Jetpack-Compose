package fi.sulku.weatherapp.data

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository(application)

    private val _location = MutableStateFlow<Location?>(null)
    val location = _location.asStateFlow()

    fun startLocationUpdates() {
        locationRepository.startLocationUpdates { location ->
            _location.value = location
        }
    }
}