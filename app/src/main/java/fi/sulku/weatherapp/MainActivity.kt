package fi.sulku.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = Color(0xFF93C5FD),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        //Current()
        //Hourly()
        //Daily()
    }
}

@Composable
fun Current() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            //Left side items:
            Column(horizontalAlignment = Alignment.Start) {
                Text("\uD83D\uDCCDLahti")
                Text("Last update: 18:15")
                Text(text = "<ConditionIcon>")
                Text(text = "14 ℃")

                Text("Sunny")
                Text("↑15.5℃ ↓6.6℃")
            }
        }
        item {
            Column(horizontalAlignment = Alignment.End) {
                Box(modifier = Modifier.height(100.dp)) //Empty space on the right
                Text("☔ Rain Chance: 84%")
                Text(text = "☃ Snow Chance: 0%")
                Text(text = "\uD83D\uDD7A Feels Like: 14.1℃")
            }
        }
    }
}