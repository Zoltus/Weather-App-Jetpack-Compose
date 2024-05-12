package fi.sulku.weatherapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import fi.sulku.weatherapp.services.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

object SettingsRepository {
    private lateinit var preferences: SharedPreferences

    private val _isDarkTheme = MutableStateFlow(false)
    private val _isFahrenheit = MutableStateFlow(false)
    private val _locale = MutableStateFlow(Locale("en"))

    val locale = _locale.asStateFlow()
    val isFahrenheit = _isFahrenheit.asStateFlow()
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun initialize(preferences: SharedPreferences) {
        this.preferences = preferences
        this._isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        this._isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        this._locale.value = Locale(preferences.getString("locale", "en") ?: "en")
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        _isDarkTheme.value = isDarkTheme
        preferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }

    fun setLocale(locale: Locale) {
        println("Set Locale: $locale")
        println("Locale was: ${_locale.value}")
        LocationService.setLocale(locale)
        _locale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
    }


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

    fun setFahrenheit(isFahrenheit: Boolean) {
        _isFahrenheit.value = isFahrenheit
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit).apply()
    }
}