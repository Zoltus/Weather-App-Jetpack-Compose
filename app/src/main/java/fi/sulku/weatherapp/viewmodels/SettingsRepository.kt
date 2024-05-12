package fi.sulku.weatherapp.viewmodels

import android.content.SharedPreferences
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
        _isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        _isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        _locale.value = Locale(preferences.getString("locale", "en") ?: "en")
    }

    fun setDarkTheme(isDarkTheme: Boolean) {
        _isDarkTheme.value = isDarkTheme
        preferences.edit().putBoolean("isDarkTheme", isDarkTheme).apply()
    }

    fun setLocale(locale: Locale) {
        _locale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
    }

    fun setFahrenheit(isFahrenheit: Boolean) {
        _isFahrenheit.value = isFahrenheit
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit).apply()
    }
}