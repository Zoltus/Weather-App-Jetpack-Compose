package fi.sulku.weatherapp.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.viewmodels.SettingsRepository

/**
 * A dialog to display the settings.
 *
 * Displays the settings to change the theme, temperature unit and language.
 * When user changes the settings, the changes are applied when the user clicks apply.
 *
 * @param viewSettings The State to control the visibility of the dialog.
 */
@Composable
fun SettingsDialog(viewSettings: MutableState<Boolean>) {
    val context = LocalContext.current
    val settings = SettingsRepository
    //Settings
    val isDarkTheme = remember { mutableStateOf(settings.isDarkTheme.value) }
    val isFahrenheit = remember { mutableStateOf(settings.isFahrenheit.value) }
    //Locales
    val locale by settings.locale.collectAsState()
    val selectedLocale = remember { mutableStateOf(locale) }

    Dialog(onDismissRequest = { viewSettings.value = false }
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SwitchSetting(text = "Dark Theme", isDarkTheme)
            SwitchSetting(text = "Fahrenheit", isFahrenheit)
            LanguageDropdown(selectedLocale)
            Spacer(modifier = Modifier.padding(6.dp))
            // Apply & Cancel Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    viewSettings.value = false
                    settings.setDarkTheme(isDarkTheme.value)
                    settings.setLocale(selectedLocale.value)
                    settings.setFahrenheit(isFahrenheit.value)
                    //Reload configs
                    settings.reloadConfig(context)
                }) {
                    Text(text = stringResource(R.string.apply))
                }
                Button(onClick = { viewSettings.value = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        }
    }
}


@Composable
private fun SwitchSetting(text: String, toggle: MutableState<Boolean>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
        Switch(
            checked = toggle.value,
            onCheckedChange = { toggle.value = it }
        )
    }
}

