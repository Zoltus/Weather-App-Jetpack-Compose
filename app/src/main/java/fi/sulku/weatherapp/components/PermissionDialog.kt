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
import androidx.compose.ui.res.stringResource
import fi.sulku.weatherapp.R
import timber.log.Timber

/**
 * A dialog that informs the user that the app needs location permissions
 * to get the current location.
 * If the user clicks "Open Settings", the app opens the settings where the user can grant the permissions.
 * If the user clicks "Dismiss", the dialog is dismissed. And user can use search by city name.
 *
 * @param permissionDialog State of dialog to control the visibility of it
 */
@Composable
fun PermissionDialog(permissionDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { permissionDialog.value = false },
        title = { Text(stringResource(id = R.string.permission_required)) },
        text = { Text(stringResource(id = R.string.permission_message)) },
        confirmButton = {
            Button(onClick = {
                Timber.d("Directing user to app settings.")
                permissionDialog.value = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }) {
                Text(stringResource(id = R.string.open_settings))
            }
        },
        dismissButton = {
            Button(onClick = { permissionDialog.value = false }) {
                Text(stringResource(id = R.string.dismiss))
            }
        }
    )
}