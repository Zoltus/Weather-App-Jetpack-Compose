package fi.sulku.weatherapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Hourly() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFBFDBFE),
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Text("HOURLY FORECAST")
        Row {
            HourlyCard()
            HourlyCard()
            HourlyCard()
            HourlyCard()
            HourlyCard()
        }
    }
}