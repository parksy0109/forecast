package com.maro.forecast.ui.loading

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition
import com.maro.forecast.R
import com.maro.forecast.domain.ForecastDatabase
import com.maro.forecast.domain.coordinate.Coordinate
import com.maro.forecast.sharedpreferences.SharePreferenceKeys
import com.maro.forecast.sharedpreferences.SharedPreferenceUtil
import com.maro.forecast.utility.ExcelUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Composable
fun LoadingScreen(
    navHostController: NavHostController,
    context: Context = LocalContext.current,
    forecastDatabase: ForecastDatabase? = ForecastDatabase.getInstance(context),
    sharedPreferenceUtil: SharedPreferenceUtil = SharedPreferenceUtil(context)
) {

    val loadingDataSource = LoadingDataSource()
    var loadingUiModel by remember { mutableStateOf(loadingDataSource.getData(1, 1, "데이터를 불러오고 있습니다.")) }

    LaunchedEffect(Unit) {
        runBlocking {
            val async = CoroutineScope(Dispatchers.IO).async {
                forecastDatabase?.coordinateDao()?.getCount() ?: 0
            }
            if(async.await() >= 3795) {
                if(sharedPreferenceUtil.getString(SharePreferenceKeys.FIRST_STEP) == null) {
                    navHostController.navigate("LocationSetting") {
                        navHostController.popBackStack()
                    }
                }else {
                    navHostController.navigate("main") {
                        navHostController.popBackStack()
                    }
                }
            }else {
                val asyncTwo = CoroutineScope(Dispatchers.IO).async {
                    initialCoordinateData(context)
                }

                val data = asyncTwo.await()
                val saveDataJob = CoroutineScope(Dispatchers.IO).async {
                    data.forEachIndexed { index, coordinate ->
                        forecastDatabase?.coordinateDao()?.insert(coordinate)
                        loadingUiModel = loadingDataSource.getData(index.plus(1), data.size, "데이터를 초기화 하고 있습니다.")
                    }
                }

                saveDataJob.await().let {
                    loadingUiModel = loadingDataSource.getData(data.size, data.size, "로딩 완료")
                    navHostController.navigate("LocationSetting") {
                        navHostController.popBackStack()
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomProgressText(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                loadingUiModel = loadingUiModel
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                Loader()
                Text(text = "hello")
            }
        }
    }
}

private fun initialCoordinateData(
    context: Context
): List<Coordinate> {
    return ExcelUtils.readCoordinateData(context)
}

@Composable
fun Loader() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            iteration = LottieConstants.IterateForever,
            speed = 1.0F
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = lottieAnimatable.progress,
            contentScale = ContentScale.FillHeight
        )
    }
}

@Composable
fun BottomProgressText(
    modifier: Modifier = Modifier,
    @PreviewParameter(SampleLoadingUiModel::class) loadingUiModel: LoadingUiModel
) {
    Column {
        Text(
            modifier = modifier,
            text = loadingUiModel.currentMessage.toString(),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = modifier,
            text = "${loadingUiModel.currentCount}/${loadingUiModel.totalCount}",
            textAlign = TextAlign.Center
        )
    }

}

