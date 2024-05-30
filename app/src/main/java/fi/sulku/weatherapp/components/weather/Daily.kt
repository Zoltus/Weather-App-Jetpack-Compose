package fi.sulku.weatherapp.components.weather

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import fi.sulku.weatherapp.Utils.convertDateToDay
import fi.sulku.weatherapp.Utils.getConvertedTempp
import fi.sulku.weatherapp.models.WeatherData
import fi.sulku.weatherapp.viewmodels.SettingsRepository
import android.graphics.Color as ColorReg

@Composable
fun Daily(weather: WeatherData, selectedDay: MutableIntState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp) // Rounded corners
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                stringResource(id = R.string.weather_daily_forecast) + "",
                fontWeight = FontWeight.Bold
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CreateDailyCards(weather, selectedDay)
                }
                WeatherChart(weather)
            }
        }
    }
}

@Composable
fun CreateDailyCards(weather: WeatherData, selectedDay: MutableIntState) {
    val context = LocalContext.current
    List(weather.daily.maxTemps.size) {
        val iconCode = weather.getConditionIconId(weather.daily.weatherCode[it])
        val timeString = convertDateToDay(context, weather.daily.time[it])
        WeatherCard(
            time = timeString,
            temp = weather.daily.maxTemps[it],
            iconId = iconCode,
            onClick = { selectedDay.intValue = it }
        )
    }
}

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
        ColorReg.WHITE
    } else {
        ColorReg.BLACK
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
                        Entry(index.toFloat(), getConvertedTempp(value, isFahrenheit).toFloat())
                    }, maxTemp).apply {
                        setCircleColor(ColorReg.WHITE)
                        valueTextColor = color
                    }
                    val minTempDataSet = LineDataSet(weather.daily.minTemps.mapIndexed { index, value ->
                        Entry(index.toFloat(), getConvertedTempp(value, isFahrenheit).toFloat())
                    }, minTemp).apply {
                        valueTextColor = color
                        setCircleColor(ColorReg.WHITE)
                    }
                    // Colors
                    rainAmountDataSet.color = ColorReg.BLUE
                    maxTempDataSet.color = ColorReg.RED
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
                    val dayNames = weather.daily.time.map { convertDateToDay(context, it) }
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