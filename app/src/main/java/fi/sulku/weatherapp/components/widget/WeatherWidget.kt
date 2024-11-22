package fi.sulku.weatherapp.components.widget


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.text.Text
import fi.sulku.weatherapp.Utils.getConvertedTemp
import fi.sulku.weatherapp.models.Location
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.models.getConditionIconId
import fi.sulku.weatherapp.models.getCurrentCondition
import fi.sulku.weatherapp.models.getLastUpdated
import fi.sulku.weatherapp.services.LocationService
import fi.sulku.weatherapp.services.WeatherApiService
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import kotlinx.coroutines.launch

/**
 * Weather widget.
 * Displays the current weather information.
 * User can click the widget to refresh the weather information.
 */
class WeatherWidget : GlanceAppWidget() {
    private val weatherApiService = WeatherApiService()

    /**
     * Provides the glance content.
     *
     * @param context The context.
     * @param id The glance id.
     */
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val currentLocation = LocationService.getCurrentLocation()
        val weather = currentLocation?.let { weatherApiService.fetchWeather(it) }
        provideContent {
            ContentView(currentLocation, weather, context)
        }
    }

    /**
     * The content view.
     *
     * User can click the widget to refresh the weather information.
     *
     * @param firstLoc The first fetched location data.
     * @param firstWeather The first fetched weather data.
     * @param context The context.
     */
    @Composable
    private fun ContentView(firstLoc: Location?, firstWeather: WeatherData?, context: Context) {
        val scope = rememberCoroutineScope()
        var location by remember { mutableStateOf(firstLoc) }
        var weather: WeatherData? by remember { mutableStateOf(firstWeather) }
        var city by remember { mutableStateOf<String?>(null) }
        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(Color.Gray)
                .clickable {
                    scope.launch {
                        isLoading = true
                        location = LocationService.getCurrentLocation()
                        weather = location?.let { weatherApiService.fetchWeather(it) }
                        //Fetch city if weather found
                        if (weather != null) {
                            city = LocationService.getCity(location)
                        }
                        isLoading = false
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            weather?.let { weather ->
                val current = weather.current
                val isFahrenheit by SettingsRepository.isFahrenheit.collectAsState()


                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = GlanceModifier.size(80.dp),
                    )
                } else {
                    Text("$city (${weather.getLastUpdated()})")
                    Image(
                        provider = ImageProvider(weather.getConditionIconId()),
                        contentDescription = "Weather icon"
                    )
                    Text(
                        weather.getCurrentCondition(context) + " " + getConvertedTemp(
                            current.temp,
                            isFahrenheit
                        )
                    )
                }
            }
            if (weather == null) {
                Text("No data found.")
            }
        }
    }
}