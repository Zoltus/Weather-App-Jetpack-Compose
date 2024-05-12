package fi.sulku.weatherapp.components.bar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.viewmodels.WeatherViewModel

@Composable
fun TopBar(vm: WeatherViewModel) {
    Box(
        modifier = Modifier
            .height(74.dp)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocationButton(vm)
            SearchField(vm, modifier = Modifier.weight(1f)) //fill remaining width
            SettingsButton()
        }
    }
}

