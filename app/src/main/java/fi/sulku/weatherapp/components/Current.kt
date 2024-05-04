package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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