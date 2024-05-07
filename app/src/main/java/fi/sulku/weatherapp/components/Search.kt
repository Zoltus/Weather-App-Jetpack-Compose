package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable

@Composable
fun Search() {
    Text(text = "Check Weather")
    Row {
        GpsLocButton()
        TextField(value = "", onValueChange = {}, placeholder = { Text("City name") })
    }
}

