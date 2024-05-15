@file:OptIn(ExperimentalMaterial3Api::class)

package fi.sulku.weatherapp.components.settings

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import java.util.Locale

/**
 * A dropdown menu to select the language.
 *
 * Displays the selected language and opens a dropdown menu to change the language.
 * The current language is not displayed in the dropdown menu as its selected.
 *
 * @param selectedLocale The selected locale to change the language.
 */
@Composable
fun LanguageDropdown(selectedLocale: MutableState<Locale>) {
    val context = LocalContext.current
    val settings = SettingsRepository
    val locales = SettingsRepository.locales
    val locale by settings.locale.collectAsState()
    var viewLocaleDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = viewLocaleDropdown,
            onExpandedChange = { viewLocaleDropdown = !viewLocaleDropdown }
        ) {
            TextField(
                value = translateLocale(context, selectedLocale.value),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewLocaleDropdown) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = viewLocaleDropdown,
                onDismissRequest = { viewLocaleDropdown = false }
            ) {
                locales.forEach {
                    if (it != locale) { // Ignores selected lang
                        DropdownMenuItem(
                            text = { Text(text = translateLocale(context, it)) },
                            onClick = {
                                viewLocaleDropdown = false
                                selectedLocale.value = it
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Translates the locale to the corresponding language for the dropdown text.
 *
 * @param context The context to access the resources.
 * @param locale The locale to translate.
 */
private fun translateLocale(context : Context, locale: Locale): String {
    return when (locale.displayName) {
        "English" -> context.getString(R.string.english)
        "Finnish" -> context.getString(R.string.finnish)
        "Swedish" -> context.getString(R.string.swedish)
        else -> context.getString(R.string.english)
    }
}