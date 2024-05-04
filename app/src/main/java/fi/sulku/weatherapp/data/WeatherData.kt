package fi.sulku.weatherapp.data

data class WeatherData(
    //Daily, current, hourly
    private val daily: Daily,
    private val current: Current,
    private val hourly: Hourly,
    val lastUpdated: Long
) {

    // Tracks if data needs to be updated
    fun needsUpdate(): Boolean {
        return System.currentTimeMillis() - lastUpdated > 5 * 60 * 1000 //10min
    }
    // Shortcut Methods to get data from WeatherData
    //Current
    fun getCurrentTemp(): Double = current.temperature_2m
    fun getCurrentTime(): String = current.time
    fun getCurrentHumidity(): Int = current.relative_humidity_2m
    fun getCurrentApparentTemp(): Double = current.apparent_temperature
    fun getCurrentPrecipitation(): Double = current.precipitation
    fun getCurrentWeatherCode(): Int = current.weather_code
    fun getCurrentWindSpeed(): Double = current.wind_speed_10m
    //Daily
    fun getDailyTimes(): List<String> = daily.time
    fun getDailyWeatherCodes(): List<Int> = daily.weather_code
    fun getDailyMaxTemps(): List<Double> = daily.temperature_2m_max
    fun getDailyMinTemps(): List<Double> = daily.temperature_2m_min
    fun getDailySunrises(): List<String> = daily.sunrise
    fun getDailySunsets(): List<String> = daily.sunset
    fun getDailyUVIndexes(): List<Double> = daily.uv_index_max
    fun getDailyPrecipitationSums(): List<Double> = daily.precipitation_sum
    fun getDailyPrecipitationProbabilitys(): List<Int> = daily.precipitation_probability_max
    //Hourly
    fun getHourlyTime(): List<String> = hourly.time
    fun getHourlyTemp(): List<Double> = hourly.temperature_2m
    fun getHourlyApparentTemp(): List<Double> = hourly.apparent_temperature
    fun getHourlyWeatherCode(): List<Int> = hourly.weather_code

    // Override and equals methods to iqnote LastUpdated time
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WeatherData) return false
        if (daily != other.daily) return false
        if (current != other.current) return false
        if (hourly != other.hourly) return false
        return true
    }

    // X 31 because of some prime stuff //todo
    override fun hashCode(): Int {
        var result = daily.hashCode()
        result = 31 * result + current.hashCode()
        result = 31 * result + hourly.hashCode()
        return result
    }
}