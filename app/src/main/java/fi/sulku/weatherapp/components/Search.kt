package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.data.WeatherViewModel
import kotlinx.coroutines.launch

/**
 * Search component.
 *
 * Allows user to get weather by his location. (User needs to allow location permissions)
 * If user denies the permission, a dialog will be shown to inform the user how to enable the permission.
 * User can also search by city name and then click the button to get the weather.
 *
 * @param vm The WeatherViewModel to access the weather data.
 * @see LocationButton
 */
@Composable
fun Search(vm: WeatherViewModel) {
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    val isLoading by vm.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Text(text = "Check Weather")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Row {
            LocationButton(vm)
            Spacer(modifier = Modifier.width(8.dp))

            Box {
                TextField(modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(20),
                    value = input,
                    onValueChange = { input = it },
                    placeholder = { Text("City name") }
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(50.dp)
                            .padding(12.dp)
                            .align(Alignment.TopEnd),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            shape = RoundedCornerShape(20),
            onClick = {
                keyboardController?.hide()
                scope.launch { vm.selectCity(input) }
            }) {
            Text("Get Weather")
        }
    }
}

