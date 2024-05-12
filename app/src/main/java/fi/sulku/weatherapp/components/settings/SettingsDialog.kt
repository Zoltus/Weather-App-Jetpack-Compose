package fi.sulku.weatherapp.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import java.util.Locale

@Composable
fun SettingsDialog(viewSettings: MutableState<Boolean>) {
    val context = LocalContext.current
    val settings = SettingsRepository
    val isDarkTheme = settings.isDarkTheme
    val isFahrenheit = settings.isFahrenheit
    val locale by settings.locale.collectAsState()
    val isEnglish = remember { mutableStateOf(locale == Locale.ENGLISH) }

    Dialog(onDismissRequest = { viewSettings.value = false }) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Setting(text = "Dark Theme", isDarkTheme)
            //Setting(text = "Fahrenheit", isFahrenheit)
            Setting(text = stringResource(R.string.language_selection), isEnglish)

            Spacer(modifier = Modifier.padding(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = {
                    viewSettings.value = false
                    settings.setDarkTheme(isDarkTheme.value)
                    if (isEnglish.value) {
                        settings.setLocale(Locale.ENGLISH)
                    } else {
                        settings.setLocale(Locale("fi"))
                    }
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
private fun Setting(text: String, toggle: MutableState<Boolean>) {
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

