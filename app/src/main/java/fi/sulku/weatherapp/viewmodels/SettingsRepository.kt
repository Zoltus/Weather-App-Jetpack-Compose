package fi.sulku.weatherapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import fi.sulku.weatherapp.services.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    /**
     * Initialize the settings repository.
     * Loads the settings from the shared preferences.
     * If the settings are not found, it uses the default.
     *
     * @param preferences Shared preferences to load the settings from.
     */
    fun initialize(preferences: SharedPreferences) {
        this.preferences = preferences
        this._isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        this._isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        this._isInches.value = preferences.getBoolean("useInches", false)
        this._isMiles.value = preferences.getBoolean("useMiles", false)
        this._selectedLocale.value =
            Locale(preferences.getString("locale", defaultLocale.toString()) ?: defaultLocale.toString())

        println("Locale: " + _selectedLocale.value)
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
        println("Set Locale: $locale")
        println("Locale was: ${_selectedLocale.value}")
        LocationService.setLocale(locale)
        _selectedLocale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
    }

    /**
     * Set the temperature unit setting.
     */
    fun setFahrenheit(isFahrenheit: Boolean) {
        _isFahrenheit.value = isFahrenheit
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit).apply()
    }

    /**
     * Set the inches/millis setting.
     */
    fun setInches(useInches: Boolean) {
        _isInches.value = useInches
        preferences.edit().putBoolean("useInches", useInches).apply()
    }

    fun setMiles(useMiles: Boolean) {
        _isMiles.value = useMiles
        preferences.edit().putBoolean("useMiles", useMiles).apply()
    }

    /**
     * Reload the configuration.
     * Reloads the config so all the changes are applied.
     *
     * @param context The context to reload the configuration for.
     */
    fun reloadConfig(context: Context) {
        val configuration = context.resources.configuration
        val resources = context.resources
        context.resources.updateConfiguration(
            context.resources.configuration,
            context.resources.displayMetrics
        )
        configuration.setLocale(_selectedLocale.value)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun getConvertedTemp(temp: Double, isFahrenheit: Boolean): String {
        return if (isFahrenheit) {
            "${String.format(_selectedLocale.value, "%.1f", temp * 9 / 5 + 32)}°F" //Format
        } else {
            "$temp°C"
        }
    }
}