package fi.sulku.weatherapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.components.bar.TopBar
import fi.sulku.weatherapp.components.weather.WeatherSection
import fi.sulku.weatherapp.services.LocationService
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import kotlinx.coroutines.launch
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(vm: WeatherViewModel) {
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    // Pull refresh wrapper
    Box(
        modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        Column {
            TopBar(vm)
            WeatherSection(vm)
        }
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                scope.launch {
                    isRefreshing = true
                    vm.selectCurrentCity() // Refetch weather
                    isRefreshing = false
                }
            }
        }
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

