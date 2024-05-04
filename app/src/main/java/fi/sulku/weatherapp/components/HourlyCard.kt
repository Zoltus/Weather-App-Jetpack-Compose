package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HourlyCard() {
    Column {
        Text("12:00")
        Text("☀️")
        Text("15℃")
    }
}