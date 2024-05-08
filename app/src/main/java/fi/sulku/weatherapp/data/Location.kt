package fi.sulku.weatherapp.data

import kotlin.math.round

data class Location(var latitude: Float, var longitude: Float) {
    init {
        latitude = roundtoClosest500m(latitude)
        longitude = roundtoClosest500m(longitude)
    }

    //Round to closest 500m to save cache and requests
    private fun roundtoClosest500m(coordinate: Float): Float {
        val coordinateInMeters = coordinate * 111139 // Convert to meters
        val roundedCoordinate = round(coordinateInMeters / 500) * 500 // Round to nearest 500 meters
        return roundedCoordinate / 111139 // Convert back to degrees
    }
}