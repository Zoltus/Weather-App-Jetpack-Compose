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
        this._isDarkTheme.value = preferences.getBoolean("isDarkTheme", false)
        this._isFahrenheit.value = preferences.getBoolean("isFahrenheit", false)
        this._locale.value = Locale(preferences.getString("locale", "en") ?: "en")
    }

    fun switchDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        preferences.edit().putBoolean("isDarkTheme", _isDarkTheme.value).apply()
    }

    fun switchLocale() {
        val locale = if (_locale.value.language == "fi") {
            Locale("en")
        } else {
            Locale("fi")
        }
        _locale.value = locale
        preferences.edit().putString("locale", locale.toString()).apply()
    }

    fun switchFahrenheit() {
        _isFahrenheit.value = !_isFahrenheit.value
        preferences.edit().putBoolean("isFahrenheit", isFahrenheit.value).apply()
    }
}