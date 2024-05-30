package fi.sulku.weatherapp.components.weather

import android.view.ViewGroup
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import fi.sulku.weatherapp.R
import fi.sulku.weatherapp.Utils
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.SettingsRepository

/**
 * A component to display the weather chart.
 * Displays the daily forecast for the next 14 days.
 *
 * @param weather The WeatherData to access the daily weather information.
 */
@Composable
fun WeatherChart(weather: WeatherData) {
    val locale by SettingsRepository.selectedLocale.collectAsState()
    val isFahrenheit by SettingsRepository.isFahrenheit.collectAsState()
    val isInches by SettingsRepository.isInches.collectAsState()
    val isDarkTheme by SettingsRepository.isDarkTheme.collectAsState()
    val rainAmount = stringResource(id = R.string.chart_rain_amount)
    val minTemp = stringResource(id = R.string.chart_min_temp)
    val maxTemp = stringResource(id = R.string.chart_max_temp)

    val color = if (isDarkTheme) {
        android.graphics.Color.WHITE
    } else {
        android.graphics.Color.BLACK
    }
    key(locale, isDarkTheme, isFahrenheit, isInches) { // Makes sure lang changes inside androidview
        AndroidView(modifier = Modifier
            .width(1030.dp)
            .height(200.dp),
            factory = { context ->
                CombinedChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val rainAmountDataSet = BarDataSet(weather.daily.rainAmount.mapIndexed { index, value ->
                        val convertedRainAmount = if (!isInches) value else value * 0.0393701
                        BarEntry(index.toFloat(), convertedRainAmount.toFloat())
                    }, rainAmount).apply {
                        valueTextColor = color
                    }
                    val maxTempDataSet = LineDataSet(weather.daily.maxTemps.mapIndexed { index, value ->
                        Entry(index.toFloat(), Utils.getConvertedTempp(value, isFahrenheit).toFloat())
                    }, maxTemp).apply {
                        setCircleColor(android.graphics.Color.WHITE)
                        valueTextColor = color
                    }
                    val minTempDataSet = LineDataSet(weather.daily.minTemps.mapIndexed { index, value ->
                        Entry(index.toFloat(), Utils.getConvertedTempp(value, isFahrenheit).toFloat())
                    }, minTemp).apply {
                        valueTextColor = color
                        setCircleColor(android.graphics.Color.WHITE)
                    }
                    // Colors
                    rainAmountDataSet.color = android.graphics.Color.BLUE
                    maxTempDataSet.color = android.graphics.Color.RED
                    minTempDataSet.color = Color(0xFF258CFF).toArgb()
                    // Data
                    val barData = BarData(rainAmountDataSet)
                    val lineData = LineData().apply {
                        addDataSet(maxTempDataSet)
                        addDataSet(minTempDataSet)
                    }
                    // Combine the data
                    val combinedData = CombinedData().apply {
                        setData(barData)
                        setData(lineData)
                    }
                    // Set CombinedData to the chart
                    this.data = combinedData
                    // List of day names
                    val dayNames = weather.daily.time.map { Utils.convertDateToDay(context, it) }
                    //Axis settings
                    xAxis.valueFormatter = IndexAxisValueFormatter(dayNames)
                    xAxis.granularity = 1f
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textColor = color
                    axisLeft.textColor = color
                    axisRight.isEnabled = false
                    legend.textColor = color
                    // Chart settings
                    description.isEnabled = false
                    setPinchZoom(false)
                    setScaleEnabled(false)
                    zoom(0.9f, 0f, 0f, 0f)
                }
            }, update = { view -> // Update the chart when the weather data changes
                view.data.notifyDataChanged()
                view.notifyDataSetChanged()
            })
    }
}