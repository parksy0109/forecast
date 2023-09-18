package com.maro.forecast.ui.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.maro.forecast.retrofit.response.UltraSrtNcst

class MainScreenViewModel : ViewModel() {

    var location = mutableStateOf("")

    var baseDateTime = mutableStateOf("")

    var items: MutableState<List<UltraSrtNcst.Response.Body.Items.Item>> = mutableStateOf(emptyList())

}