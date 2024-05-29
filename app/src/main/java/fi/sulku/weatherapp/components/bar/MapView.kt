package fi.sulku.weatherapp.components.bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import fi.sulku.weatherapp.models.Location
import fi.sulku.weatherapp.viewmodels.WeatherViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun MapView(weatherVm: WeatherViewModel, scope: CoroutineScope, showMap: MutableState<Boolean>) {
    val selectedLoc: Location? by weatherVm.selectedLocation.collectAsState()
    // Default loc to tampere if none selected
    val defaultLoc = LatLng(61.504062480317444, 23.830518539846498)

    val latLng by remember {
        mutableStateOf(selectedLoc?.let { LatLng(it.latitude, it.longitude) } ?: defaultLoc)
    }
    // Marker position to show current spot
    val markerPosition by remember { mutableStateOf(latLng) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(latLng, 10f, 0f, 0f)
    }

    Dialog(
        onDismissRequest = { showMap.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLongClick = {
                showMap.value = false
                scope.launch {
                    Timber.d("Selecting location from map")
                    weatherVm.selectLocation(it.latitude, it.longitude)
                }
            },
        ) {
            selectedLoc?.let {
                Marker(position = markerPosition, draggable = false)
            }
        }
    }
}
