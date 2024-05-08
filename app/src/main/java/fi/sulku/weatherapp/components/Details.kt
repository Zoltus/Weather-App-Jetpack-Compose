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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.data.WeatherData

/**
 * A component to display the weather details.
 *
 * Creates Vertical Grid with 2 columns to display the weather details.
 * Half on the left and half on the right side.
 *
 * @param weather The WeatherData to access the weather details.
 */
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
                    Text("Rain")
                    Text(weather.daily.rainAmount[1].toString())
                    Text("Humidity")
                    Text(weather.current.humidity.toString())
                    Text("UV")
                    Text(weather.daily.uv_index_max[1].toString())
                    Text("Sunrise")
                    Text(weather.getDateAsClockTime(weather.daily.sunrise[1]))
                }
            }
            item {
                Column(horizontalAlignment = Alignment.End) {
                    Text("Wind Speed")
                    Text(weather.current.windSpeed.toString())
                    //Text("Visibility")
                    //Text("")
                    Text("Pressure")
                    Text( "${weather.current.pressure} hPa")
                    Text("SunSet")
                    Text(weather.getDateAsClockTime(weather.daily.sunset[1]))
                }
            }
        }
    }
}