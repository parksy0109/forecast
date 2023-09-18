package com.maro.forecast.ui.locationsetting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.maro.forecast.data.LocationClassification

class LocationSettingViewModel : ViewModel() {

    var firstStep = mutableStateOf("")
    var secondStep = mutableStateOf("")
    var thirdStep = mutableStateOf("")

    var locationClassificationFirst: MutableState<Map<String, List<LocationClassification>>> = mutableStateOf(emptyMap())
    val locationClassificationSecond: MutableState<Map<String, List<LocationClassification>>>
        get() = mutableStateOf(locationClassificationFirst.value[firstStep.value]!!.groupBy { item -> item.secondStep })

    val locationClassificationThird: MutableState<Map<String, List<LocationClassification>>>
        get() = mutableStateOf(locationClassificationSecond.value[secondStep.value]!!.groupBy { item -> item.thirdStep ?: "" })

    val firstStepIsSelect: MutableState<List<Pair<String, MutableState<Boolean>>>> = mutableStateOf(emptyList())

    val secondStepIsSelect: MutableState<List<Pair<String, MutableState<Boolean>>>> = mutableStateOf(emptyList())

    val thirdStepIsSelect: MutableState<List<Pair<String, MutableState<Boolean>>>> = mutableStateOf(emptyList())

    fun checkStepDataIsNotEmpty(): Boolean {
        return (firstStep.value.isNotEmpty() && secondStep.value.isNotEmpty() && thirdStep.value.isNotEmpty())
    }

}