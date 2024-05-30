package fi.sulku.weatherapp.components.bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchField(vm: WeatherViewModel, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val isLoading by vm.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val input = remember { mutableStateOf("") }
    val locale by SettingsRepository.selectedLocale.collectAsState()

    var isError by remember { mutableStateOf(false) }


    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        TextField(
            isError = isError,
            enabled = !isLoading,
            modifier = Modifier.fillMaxHeight(),
            singleLine = true,
            shape = RoundedCornerShape(10),
            value = input.value,
            onValueChange = {
                input.value = it
                isError = false
            },
            placeholder = {
                val unused = locale // Listen for changes in the locale to refresh placeholder(jetbrains bug)
                Text(stringResource(R.string.weather_city))
            },
            colors = TextFieldDefaults.colors(
                // Remove underline
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    scope.launch {
                        val weatherData = vm.selectCity(input.value)
                        input.value = ""
                        // if invalid city show error
                        if (weatherData == null) {
                            isError = true
                        }
                    }
                }
            ),
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .padding(top = 7.dp, end = 16.dp)
                    .align(Alignment.CenterEnd),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}