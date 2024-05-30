package fi.sulku.weatherapp.components.bar

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.weatherapp.components.PermissionDialog
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
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
fun LocationButton(vm: WeatherViewModel) {
    val scope = rememberCoroutineScope()
    val showPermDialog = remember { mutableStateOf(false) }
    val showMap = remember { mutableStateOf(false) }
    var isChoosingLocation by remember { mutableStateOf(false) }
    // Premissions
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
                    vm.selectCurrentCity()
                } else {
                    showPermDialog.value = true
                }
            }
        }
    )

    Button(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(10),
        onClick = {
            isChoosingLocation = !isChoosingLocation
            if (isChoosingLocation) {
                permLauncher.launch(perms)
            }
        }
    ) {
        Text(if (isChoosingLocation) "\uD83D\uDCCD" else "\uD83C\uDF0D")
    }

    if (isChoosingLocation) {
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f),
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(10),
            onClick = {
                isChoosingLocation = !isChoosingLocation
                showMap.value = true
            },
        ) {
            Text("\uD83D\uDDFA\uFE0F")
        }
    }

    if (showPermDialog.value) {
        PermissionDialog(showPermDialog)
    }
    if (showMap.value) {
        MapDialog(vm, scope, showMap)
    }

    LaunchedEffect(key1 = Unit) {
        permLauncher.launch(perms)
    }
    // Long click detection
}

