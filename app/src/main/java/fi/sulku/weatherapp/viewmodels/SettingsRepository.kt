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
    private val _locale = MutableStateFlow(defaultLocale)
    // Selected locale accessor.
    val locale = _locale.asStateFlow()
    val isFahrenheit = _isFahrenheit.asStateFlow()
    val isDarkTheme = _isDarkTheme.asStateFlow()

    /**
     * Initialize the settings repository.
     * Loads the settings from the shared preferences.
     * If the settings are not found, it uses the default.
     *
     * @param preferences Shared preferences to load the settings from.
     */
    fun initialize(preferences: SharedPreferences) {
        this.preferences = preferences
        this._isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        this._isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        this._locale.value =
            Locale(preferences.getString("locale", defaultLocale.toString()) ?: defaultLocale.toString())

        println("Locale: " + _locale.value)
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
        println("Locale was: ${_locale.value}")
        LocationService.setLocale(locale)
        _locale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
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
        configuration.setLocale(_locale.value)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    /**
     * Set the temperature unit setting.
     */
    fun setFahrenheit(isFahrenheit: Boolean) {
        _isFahrenheit.value = isFahrenheit
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit).apply()
    }
}