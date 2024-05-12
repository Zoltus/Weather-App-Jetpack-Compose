package fi.sulku.weatherapp.components.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.viewmodels.WeatherViewModel

@Composable
fun SearchTextField(vm: WeatherViewModel, input: MutableState<String>) {
    val isLoading by vm.isLoading.collectAsState()
    Box {
        TextField(modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
            shape = RoundedCornerShape(20),
            value = input.value,
            onValueChange = { input.value = it },
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