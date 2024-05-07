package fi.sulku.weatherapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.components.Current
import fi.sulku.weatherapp.components.Daily
import fi.sulku.weatherapp.components.Details
import fi.sulku.weatherapp.components.Hourly
import fi.sulku.weatherapp.components.Search
import fi.sulku.weatherapp.data.LocationViewModel
import fi.sulku.weatherapp.data.WeatherViewModel
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Search()
        WeatherSection()
    }
}

@Composable
fun WeatherSection() {
    val scope = rememberCoroutineScope()
    val vm : WeatherViewModel = viewModel()
    val lv: LocationViewModel = viewModel()
    val location = lv.location.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Check if all requested permissions have been granted
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                lv.startLocationUpdates()
            }
        }
    )


    if (location.value == null) {
        Text(text = "Location: Not available")
    }

    location.value?.let {
        Text(text = "Location: ${it.latitude}, ${it.longitude}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color(0xFF93C5FD),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Button(onClick = {
            scope.launch {
                //WeatherApiService.getWeather(Coordinates(location.value!!.latitude.toFloat(), location.value!!.longitude.toFloat()), scope)
                println("Get getWeather for current location")
            }
        }) {
            Text(text = "Get Current Location Weather")
        }


        Current()
        Hourly()
        Daily()
        Details()
    }

    LaunchedEffect(Unit) {
        println("@@@@@location Launcheffect")
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
        lv.startLocationUpdates()
    }
}
