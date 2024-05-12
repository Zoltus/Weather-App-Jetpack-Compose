package fi.sulku.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.components.weather.Current
import fi.sulku.weatherapp.components.weather.Details
import fi.sulku.weatherapp.components.weather.Hourly
import fi.sulku.weatherapp.components.search.SearchBar
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val vm: WeatherViewModel = viewModel()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(vm)
        WeatherSection(vm)
    }
}

@Composable
fun WeatherSection(vm: WeatherViewModel) {
    val selectedWeather by vm.selectedWeather.collectAsState()
    selectedWeather?.let { weather ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    color = Color(0xFF93C5FD),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Current(vm, weather)
            Hourly(weather)
            //Daily()
            Details(weather)
        }
    }
}