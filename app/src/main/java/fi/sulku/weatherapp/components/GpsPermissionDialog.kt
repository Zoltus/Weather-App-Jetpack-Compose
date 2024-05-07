package fi.sulku.weatherapp.components

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext

@Composable
fun GpsPermissionDialog(showDialog: MutableState<Boolean>) {
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