package fi.sulku.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.components.bar.TopBar
import fi.sulku.weatherapp.components.weather.Current
import fi.sulku.weatherapp.components.weather.Daily
import fi.sulku.weatherapp.components.weather.Details
import fi.sulku.weatherapp.components.weather.Hourly
import fi.sulku.weatherapp.services.LocationService
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
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
 *
 */
@Composable
fun WeatherApp(vm: WeatherViewModel) {
    Column {
        TopBar(vm)
        WeatherSection(vm)
    }
}

/**
 * Weather section
 *
 * Displays all weather related stuff
 *
 * @param weatherVm The WeatherViewModel to access the weather information.
 */
@Composable
fun WeatherSection(weatherVm: WeatherViewModel) {
    val selectedWeather by weatherVm.selectedWeather.collectAsState()
    //todo to vm?
    val selectedDay = remember { mutableIntStateOf(1) }

    //If no weather data is selected, show loading text
    if (selectedWeather == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading Weather Data...")
        }
    }

    selectedWeather?.let { weather ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)) //Bottom rounded corners
        ) {
            item { Current(weatherVm, weather) }
            item { Spacer(modifier = Modifier.padding(10.dp)) }
            item { Hourly(weather, selectedDay.intValue) }
            item { Daily(weather, selectedDay) }
            item { Spacer(modifier = Modifier.padding(10.dp)) }
            item { Details(weather.daily, selectedDay.intValue) }
        }
    }
}