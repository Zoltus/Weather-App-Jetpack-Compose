package fi.sulku.weatherapp.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.data.WeatherViewModel
import kotlinx.coroutines.launch

/**
 * A button to fetch the weather data based on the GPS location.
 * When clicked, it asks for the location permission.
 * If the permission is denied, it shows a dialog to inform the user
 * and tell them how to enable the permission.
 *
 * @see PermissionDialog
 */
@Composable
fun LocationButton() {
    val weatherVm: WeatherViewModel = viewModel()
    val showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val permLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            scope.launch {
                val hasPermissions = permissions.entries.all { it.value }
                if (hasPermissions) {
                    weatherVm.selectCurrentCity()
                } else {
                    showDialog.value = true
                }
            }
        }
    )

    Button(modifier = Modifier.size(50.dp),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20),
        onClick = { permLauncher.launch(perms) }
    ) {
        Text("📍")
    }

    if (showDialog.value) {
        PermissionDialog(showDialog)
    }

    LaunchedEffect(key1 = Unit) {
        permLauncher.launch(perms)
    }
}

