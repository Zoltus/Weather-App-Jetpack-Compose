package fi.sulku.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.components.weather.WeatherSection
import fi.sulku.weatherapp.components.bar.TopBar
import fi.sulku.weatherapp.services.LocationService
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import timber.log.Timber

/**
 * Main activity of the application.
 */
class MainActivity : ComponentActivity() {
    /**
     * First initializes the settings repository and location service.
     * then it fetches the old location if it exists.
     * After that it reloads configs so locale changes are applied correctly.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set timber as debugger
        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
      //  }
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val weatherVm: WeatherViewModel = viewModel()
            val isDarkTheme by SettingsRepository.isDarkTheme.collectAsState()
            //Set locale to viewmodels locale
            val locale by SettingsRepository.selectedLocale.collectAsState()
            Timber.d("Using locale: $locale")
            //Initialize LocationService and SettingsRepository
            Timber.d("Initializing LocationService")
            LocationService.initialize(this.application)
            Timber.d("Initializing settings repository")
            SettingsRepository.initialize(getSharedPreferences("settings", Context.MODE_PRIVATE))
            //Fetches old location if it exists
            SettingsRepository.fetchOldLocation(weatherVm, scope)
            //Reloads langues from configs so eveything updates correcly
            SettingsRepository.reloadConfig(context)
            WeatherAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp(weatherVm)
                }
            }
        }
    }
}

/**
 * WeatherApp
 * Has topbar and weathersection.
 *
 * @param vm The WeatherViewModel to access the weather data.
 *
 */
@Composable
fun WeatherApp(vm: WeatherViewModel) {
    Column {
        TopBar(vm)
        WeatherSection(vm)
    }
}

