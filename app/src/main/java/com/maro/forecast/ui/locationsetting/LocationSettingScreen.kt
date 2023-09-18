package com.maro.forecast.ui.locationsetting

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.maro.forecast.sharedpreferences.SharePreferenceKeys
import com.maro.forecast.sharedpreferences.SharedPreferenceUtil
import com.maro.forecast.ui.theme.PositiveButtonColor
import com.maro.forecast.utility.ExcelUtils


@Composable
fun LocationSettingScreen(
    navHostController: NavHostController,
    context: Context = LocalContext.current,
    sharedPreferenceUtil: SharedPreferenceUtil = SharedPreferenceUtil(context),
    locationSettingViewModel: LocationSettingViewModel = viewModel()
) {

    locationSettingViewModel.locationClassificationFirst.value = ExcelUtils.getLocationClassification(context)
    locationSettingViewModel.firstStepIsSelect.value = locationSettingViewModel.locationClassificationFirst.value.keys.map { it to mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            SaveButton {
                if(locationSettingViewModel.checkStepDataIsNotEmpty()) {
                    sharedPreferenceUtil.setString(SharePreferenceKeys.FIRST_STEP, locationSettingViewModel.firstStep.value)
                    sharedPreferenceUtil.setString(SharePreferenceKeys.SECOND_STEP, locationSettingViewModel.secondStep.value)
                    sharedPreferenceUtil.setString(SharePreferenceKeys.THIRD_STEP, locationSettingViewModel.thirdStep.value)
                    navHostController.navigate("main") {
                        navHostController.popBackStack()
                    }
                }else {
                    Toast.makeText(context, "날씨 구역을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text("날씨 구역")
                Chips(locationSettingViewModel.firstStepIsSelect.value) { index, name ->
                    for (i in 0 until locationSettingViewModel.firstStepIsSelect.value.size) {
                        locationSettingViewModel.firstStepIsSelect.value[i].second.value = false
                    }
                    locationSettingViewModel.firstStepIsSelect.value[index].second.value = true
                    locationSettingViewModel.firstStep.value = name

                    locationSettingViewModel.secondStepIsSelect.value =
                        locationSettingViewModel.locationClassificationSecond.value.keys.map { it to mutableStateOf(false) }
                }
                if (locationSettingViewModel.firstStep.value.isNotEmpty()) {
                    SecondSteps(
                        secondStepIsSelect = locationSettingViewModel.secondStepIsSelect.value
                    ) { index, name ->
                        for (i in 0 until locationSettingViewModel.secondStepIsSelect.value.size) {
                            locationSettingViewModel.secondStepIsSelect.value[i].second.value = false
                        }
                        locationSettingViewModel.secondStepIsSelect.value[index].second.value = true
                        locationSettingViewModel.secondStep.value = name
                        locationSettingViewModel.thirdStepIsSelect.value = locationSettingViewModel.locationClassificationThird.value.keys.map { it to mutableStateOf(false) }
                    }
                }
                if (locationSettingViewModel.secondStep.value.isNotEmpty()) {
                    SecondSteps(secondStepIsSelect = locationSettingViewModel.thirdStepIsSelect.value) { index, name ->
                        for (i in 0 until locationSettingViewModel.thirdStepIsSelect.value.size) {
                            locationSettingViewModel.thirdStepIsSelect.value[i].second.value = false
                        }
                        locationSettingViewModel.thirdStepIsSelect.value[index].second.value = true
                        locationSettingViewModel.thirdStep.value = name
                    }
                }

                Text("${locationSettingViewModel.firstStep.value}, ${locationSettingViewModel.secondStep.value}, ${locationSettingViewModel.thirdStep.value}")
            }
        }

    }
}

@Composable
fun SecondSteps(
    secondStepIsSelect: List<Pair<String, MutableState<Boolean>>>,
    setOnClickListener: (Int, String) -> Unit
) {
    Chips(secondStepIsSelect) { index, name ->
        setOnClickListener(index, name)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chips(
    locationCoordinates: List<Pair<String, State<Boolean>>>,
    setOnClickListener: (Int, String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(0.dp, 10.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(locationCoordinates) { index, item ->
            FilterChip(
                selected = item.second.value,
                onClick = {
                    setOnClickListener(index, item.first)
                },
                label = { Text(item.first) }
            )
        }
    }
}

@Composable
fun SaveButton(
    setOnClickListener: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                setOnClickListener()
            }
            .height(48.dp)
            .background(PositiveButtonColor),
        contentAlignment = Alignment.Center
    ) {
        Text("설정하기")
    }
}