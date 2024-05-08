package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.data.WeatherApiService

@Composable
fun Search() {
    var input by remember { mutableStateOf("") }

    Text(text = "Check Weather")
    Row {
        GpsLocButton()
        Spacer(modifier = Modifier.width(8.dp))
        TextField(modifier = Modifier.height(50.dp),
            shape = RoundedCornerShape(20),
            value = input, onValueChange = { input = it },
            placeholder = { Text("City name") }
        )
    }
    Button(onClick = {

    }) {
        Text("Get Weather")
    }
}

