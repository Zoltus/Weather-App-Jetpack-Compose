package fi.sulku.weatherapp.data

data class WeatherData(
    val weather: WeatherResponse,
    val hourly: HourlyResponse,
    val lastUpdated: Long
) {
    fun needsUpdate(): Boolean {
        return System.currentTimeMillis() - lastUpdated > 5 * 60 * 1000 //10min
    }

    // Override and equals methods to iqnote LastUpdated time
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WeatherData) return false
        if (weather != other.weather) return false
        if (hourly != other.hourly) return false
        return true
    }

    override fun hashCode(): Int {
        var result = weather.hashCode()
        result = 31 * result + hourly.hashCode()
        return result
    }
}