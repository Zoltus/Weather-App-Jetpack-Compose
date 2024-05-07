package fi.sulku.weatherapp.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.sulku.weatherapp.data.LocationViewModel

@Composable
fun AskLocation() {
    val lv: LocationViewModel = viewModel()
    val location = lv.location.collectAsState()
    val showDialog: MutableState<Boolean> = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            // Check if all requested permissions have been granted
            val allPermissionsGranted = permissions.entries.all { it.value }
            if (allPermissionsGranted) {
                //if granted start location updates
                lv.startLocationUpdates()
            } else {
                //if denied show dialog
                showDialog.value = true
            }
        }
    )

    if (location.value == null) {
        Text(text = "Location: Not available")
    }

    location.value?.let {
        Text(text = "Location: ${it.latitude}, ${it.longitude}")
    }

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
        PermissionDialog(showDialog)
    }
}

@Composable
fun PermissionDialog(showDialog : MutableState<Boolean>) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = { showDialog.value = false },
        title = { Text("Permission required") },
        text = { Text("To get your current location the app needs location permissions. Please grant the permissions or search by city name.") },
        confirmButton = {
            Button(onClick = {
                showDialog.value = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            Button(onClick = { showDialog.value = false }) {
                Text("Dismiss")
            }
        }
    )
}