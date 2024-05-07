package fi.sulku.weatherapp.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.data.WeatherViewModel
import kotlinx.coroutines.launch

@Composable
fun GpsLocButton() {
    val weatherVm: WeatherViewModel = viewModel()
    val showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            scope.launch {
                // Check if all requested permissions have been granted
                val allPermissionsGranted = permissions.entries.all { it.value }
                if (allPermissionsGranted) {
                    weatherVm.setCurrentLocation() //if granted start location updates
                } else {
                    showDialog.value = true //if denied show dialog
                }
            }
        }
    )

    Button(onClick = {
        //Ask permission
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }) {
        Text("📍")
    }

    if (showDialog.value) {
        GpsPermissionDialog(showDialog)
    }
}

