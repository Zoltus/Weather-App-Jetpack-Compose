package fi.sulku.weatherapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Details() {
    Text("DETAILS")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
            Text("DETAILS")
        }
        item {
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