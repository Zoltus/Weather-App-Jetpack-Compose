package fi.sulku.weatherapp.models

import kotlinx.serialization.Serializable
import timber.log.Timber
import kotlin.math.round

/**
 * Data class for location.
 * Latitude and longitude are rounded to closest 500m to reduce cache size and api requests.
 *
 * @param latitude Latitude of the location.
 * @param longitude Longitude of the location.
 */
@Serializable
data class Location(var latitude: Double, var longitude: Double) {
    init {
        latitude = roundtoClosest500m(latitude)
        longitude = roundtoClosest500m(longitude)
    }

    /**
     * Round the latitude and longitude to closest 500m.
     *
     * @param coordinate Latitude or longitude.
     */
    private fun roundtoClosest500m(coordinate: Double): Double {
        Timber.d("Rounding $coordinate")
        val coordinateInMeters = coordinate * 111139 // Convert to meters
        val roundedCoordinate = round(coordinateInMeters / 500) * 500 // Round to nearest 500 meters
        return roundedCoordinate / 111139 // Convert back to degrees
    }
}