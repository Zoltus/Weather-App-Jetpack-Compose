package fi.sulku.weatherapp.data


//todo should WeatherData also have Location?
data class WeatherData(
    val daily: Daily,
    val current: Current,
    val hourly: Hourly,
    val lastUpdated: Long
) {
    // Tracks if data needs to be updated
    fun needsUpdate(): Boolean {
        return System.currentTimeMillis() - lastUpdated > 5 * 60 * 1000 //10min
    }

    fun getCurrentCondition() : String {
        return getCondition(current.weather_code)
    }

    fun getCondition(weatherCode : Int) : String {
        return when (weatherCode) {
            0 -> "Selkeää"
            in 1..3 -> "Puolipilvistä"
            in 45..48 -> "Sumua"
            in 51..55 -> "Tihkusadetta"
            in 56..57 -> "Jäätävää tihkua"
            in 61..65 -> "Sadetta"
            in 66..67 -> "Jäätävää sadetta"
            in 71..75 -> "Lumisadetta"
            77 -> "Lumirakeita"
            in 80..82 -> "Sadekuuroja"
            in 85..86 -> "Lumikuuroja"
            in 95..96 -> "Ukkosta"
            99 -> "Voimakasta ukkosta"
            else -> "Tuntematon"
        }
    }
}

/*
0	Clear sky
1, 2, 3	Mainly clear, partly cloudy, and overcast
45, 48	Fog and depositing rime fog
51, 53, 55	Drizzle: Light, moderate, and dense intensity
56, 57	Freezing Drizzle: Light and dense intensity
61, 63, 65	Rain: Slight, moderate and heavy intensity
66, 67	Freezing Rain: Light and heavy intensity
71, 73, 75	Snow fall: Slight, moderate, and heavy intensity
77	Snow grains
80, 81, 82	Rain showers: Slight, moderate, and violent
85, 86	Snow showers slight and heavy
95 *	Thunderstorm: Slight or moderate
96, 99 *	Thunderstorm with slight and heavy hail
*/

/*
0 -> "Clear sky"
in 1..3 -> "Partly cloudy"
in 45..48 -> "Fog"
in 51..55 -> "Drizzle"
in 56..57 -> "Freezing Drizzle"
in 61..65 -> "Rain"
in 66..67 -> "Freezing Rain"
in 71..75 -> "Snow"
77 -> "Snow grains"
in 80..82 -> "Rain showers"
in 85..86 -> "Snow showers"
in 95..96 -> "Thunderstorm"
99 -> "Heavy hail"
else -> "Unknown"*/
