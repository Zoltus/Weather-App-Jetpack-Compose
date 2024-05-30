package fi.sulku.weatherapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import fi.sulku.weatherapp.models.Location
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.services.LocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.Locale

/**
 * Repository for the settings.
 */
object SettingsRepository {
    /**
     * Shared preferences to store the settings.
     * MUST BE INITIALIZED BEFORE USING THE SETTINGS REPOSITORY.
     */
    private lateinit var preferences: SharedPreferences
    // Dark theme setting, true if the dark theme is enabled, false otherwise.
    private val _isDarkTheme = MutableStateFlow(false)
    // Temperature unit, true to use Fahrenheit, false to use Celsius.
    private val _isFahrenheit = MutableStateFlow(false)
    private val _isInches = MutableStateFlow(false)
    private val _isMiles = MutableStateFlow(false)

    // Default to tampere
    private val defaultLocation = Location(61.4981, 23.7610)
    private val _lastSelectedLocation: MutableStateFlow<Location> = MutableStateFlow(defaultLocation)

    private val _lastSelectedWeather: MutableStateFlow<WeatherData?> = MutableStateFlow(null)

    private val _locales = listOf(
        Locale("en"),
        Locale("fi"),
        Locale("sv")
    )

    /**
     * Access the list of available locales.
     */
    val locales: List<Locale> get() = _locales.toList()

    // Default locale to use if the locale is not found.
    private val defaultLocale = locales[2]

    // Selected locale.
    private val _selectedLocale = MutableStateFlow(defaultLocale)

    // Selected locale accessor.
    val selectedLocale = _selectedLocale.asStateFlow()
    val isFahrenheit = _isFahrenheit.asStateFlow()
    val isDarkTheme = _isDarkTheme.asStateFlow()
    val isMiles = _isMiles.asStateFlow()
    val isInches = _isInches.asStateFlow()
    val lastSelectedLocation = _lastSelectedLocation.asStateFlow()
    val lastSelectedWeather = _lastSelectedWeather.asStateFlow()

    /**
     * Initialize the settings repository.
     * Loads the settings from the shared preferences.
     * If the settings are not found, it uses the default.
     *
     * @param preferences Shared preferences to load the settings from.
     */
    fun initialize(weatherVm: WeatherViewModel, scope: CoroutineScope, preferences: SharedPreferences) {
        this.preferences = preferences
        this._isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        this._isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        this._isInches.value = preferences.getBoolean("useInches", false)
        this._isMiles.value = preferences.getBoolean("useMiles", false)
        this._selectedLocale.value =
            Locale(preferences.getString("locale", defaultLocale.toString()) ?: defaultLocale.toString())
        val lat = preferences.getFloat("lastloc_lat", defaultLocation.latitude.toFloat())
        val lon = preferences.getFloat("lastloc_lon", defaultLocation.longitude.toFloat())
        this._lastSelectedLocation.value = Location(lat.toDouble(), lon.toDouble())

        //Get old weatherData
        val weatherJson = preferences.getString("lastweather", null)
        // Set old locs
        if (weatherJson != null) {
            val weatherData: WeatherData = Json.decodeFromString<WeatherData>(weatherJson)
            this._lastSelectedWeather.value = weatherData
        }

        // Fetch updates for the old/Default loc
        scope.launch {
            weatherVm.selectLocation(lat.toDouble(), lon.toDouble())
        }
    }

    /**
     * Set the dark theme setting.
     *
     * @param isDarkTheme true if the dark theme is enabled, false otherwise.
     */
    fun setDarkTheme(isDarkTheme: Boolean) {
        _isDarkTheme.value = isDarkTheme
        preferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }

    /**
     * Set the locale setting.
     *
     * @param locale The locale to set.
     */
    fun setLocale(locale: Locale) {
        Timber.d("Setting locale to: $locale")
        LocationService.setLocale(locale)
        _selectedLocale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
    }

    /**
     * Set the temperature unit setting.
     */
    fun setFahrenheit(isFahrenheit: Boolean) {
        Timber.d("Setting fahrenheit to: $isFahrenheit")
        _isFahrenheit.value = isFahrenheit
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit).apply()
    }

    /**
     * Set the inches/millis setting.
     */
    fun setInches(isInches: Boolean) {
        Timber.d("Setting inches to: $isInches")
        _isInches.value = isInches
        preferences.edit().putBoolean("useInches", isInches).apply()
    }

    fun setMiles(isMiles: Boolean) {
        Timber.d("Setting miles to: $isMiles")
        _isMiles.value = isMiles
        preferences.edit().putBoolean("useMiles", isMiles).apply()
    }

    fun setLastLocation(loc: Location) {
        Timber.d("Setting last location to: $loc")
        _lastSelectedLocation.value = loc
        preferences.edit().putFloat("lastloc_lat", loc.latitude.toFloat()).apply()
        preferences.edit().putFloat("lastloc_lon", loc.longitude.toFloat()).apply()
    }

    fun setLastWeather(weatherData: WeatherData) {
        Timber.d("Setting last weather to: $weatherData")
        _lastSelectedWeather.value = weatherData
        val jsonString = Json.encodeToString(weatherData)
        preferences.edit().putString("lastweather", jsonString).apply()
    }

    /**
     * Reload the configuration.
     * Reloads the config so all the changes are applied.
     *
     * @param context The context to reload the configuration for.
     */
    fun reloadConfig(context: Context) {
        Timber.d("Reloading configs")
        val configuration = context.resources.configuration
        val resources = context.resources
        context.resources.updateConfiguration(
            context.resources.configuration,
            context.resources.displayMetrics
        )
        configuration.setLocale(_selectedLocale.value)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}