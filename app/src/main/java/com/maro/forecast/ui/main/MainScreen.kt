package com.maro.forecast.ui.main

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maro.forecast.data.ActualValue
import com.maro.forecast.data.PTYValue
import com.maro.forecast.domain.ForecastDatabase
import com.maro.forecast.retrofit.PublicDataAPIRepository
import com.maro.forecast.retrofit.response.UltraSrtNcst
import com.maro.forecast.sharedpreferences.SharePreferenceKeys
import com.maro.forecast.sharedpreferences.SharedPreferenceUtil
import com.maro.forecast.utility.TimeUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun MainScreen(
    context: Context = LocalContext.current,
    forecastDatabase: ForecastDatabase? = ForecastDatabase.getInstance(context),
    sharedPreferenceUtil: SharedPreferenceUtil = SharedPreferenceUtil(context),
    publicDataAPIRepository: PublicDataAPIRepository = PublicDataAPIRepository(),
    mainScreenViewModel: MainScreenViewModel = viewModel()
) {
    var responseData by remember { mutableStateOf("메인 화면 입니다.") }
    val rememberScrollState = rememberScrollState()
    var hasData by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        val async = CoroutineScope(Dispatchers.IO).async {
            val string = sharedPreferenceUtil.getString(SharePreferenceKeys.FIRST_STEP)!!
            val string1 = sharedPreferenceUtil.getString(SharePreferenceKeys.SECOND_STEP)!!
            val string2 = sharedPreferenceUtil.getString(SharePreferenceKeys.THIRD_STEP)!!

            mainScreenViewModel.location.value = "$string $string1 $string2"
            forecastDatabase?.coordinateDao()?.findByStep(string, string1, string2)
        }

        val coordinate = async.await()

        coordinate?.let {
            val job = CoroutineScope(Dispatchers.IO).async {
                val baseDate = TimeUtils.getBaseDate(LocalDate.now())
                val baseTime = TimeUtils.getBaseTime(LocalDateTime.now())

                mainScreenViewModel.baseDateTime.value = "$baseDate $baseTime"
                publicDataAPIRepository.getCurrentUltraSrtNcst(
                    baseDate,
                    baseTime,
                    coordinate
                )
            }

            job.await().let { response ->
                if (response.isSuccessful) {
                    mainScreenViewModel.items.value = response.body()?.response?.body?.items?.item ?: emptyList()
                } else {
                    responseData = "날씨 데이터를 가져오는데 오류가 발생하였습니다."
                }
            }
        }
    }

    if (mainScreenViewModel.location.value.isNotEmpty()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                MainTopBar{

                }
            }
        ) {
            MainScreenView(it)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = responseData
            )
        }
    }

}

@Composable
fun MainTopBar(
    setOnClickSettingListener: () -> Unit,
) {

    Box(
        modifier = Modifier
            .padding()
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.CenterEnd
    ) {

        IconButton(onClick = { setOnClickSettingListener() }) {
            Icon(Icons.Default.Settings, contentDescription = null)
        }

    }

}

@Composable
fun MainScreenView(
    paddingValues: PaddingValues,
    mainScreenViewModel: MainScreenViewModel = viewModel()
) {

    val rememberScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState)
            .padding(paddingValues)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)
                .height(48.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(mainScreenViewModel.location.value)
            Text(mainScreenViewModel.baseDateTime.value)
        }
        WeatherItems()
    }
}

@Composable
fun WeatherItems(
    mainScreenViewModel: MainScreenViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 10.dp)
    ) {
        mainScreenViewModel.items.let {
            it.value.forEach {
                WeatherItem(it)
            }
        }
    }
}

@Composable
fun WeatherItem(item: UltraSrtNcst.Response.Body.Items.Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val findByActualValue = ActualValue.findByActualValue(item.category)!!
            Text(findByActualValue.itemName)
            val obsrValue = if (item.category == ActualValue.PTY.name) {
                PTYValue.findByCode(item.obsrValue.toInt())?.description ?: "데이터 없음"
            } else {
                item.obsrValue + findByActualValue.unit
            }
            Text(text = obsrValue)
        }
    }
}