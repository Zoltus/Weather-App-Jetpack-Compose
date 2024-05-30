package fi.sulku.weatherapp.components.bar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.components.settings.SettingsDialog

/**
 * Button to open the settings dialog.
 */
@Composable
fun SettingsButton() {
    val viewSettings = remember { mutableStateOf(false) }
    Button(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        shape = RoundedCornerShape(10),
        contentPadding = PaddingValues(0.dp),
        onClick = { viewSettings.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.settings_title),
            tint = Color.White,
        )
    }

    if (viewSettings.value) {
        SettingsDialog(viewSettings)
    }

}