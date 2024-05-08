package fi.sulku.weatherapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.data.WeatherData

@Composable
fun Details(weather: WeatherData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFBFDBFE),
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Text("DETAILS")
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Column {
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                }
            }
            item {
                Column {
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                    Text("DETAILS")
                }
            }
        }
    }
}