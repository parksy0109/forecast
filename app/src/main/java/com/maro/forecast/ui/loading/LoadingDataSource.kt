package com.maro.forecast.ui.loading

class LoadingDataSource {

    fun getData(currentCount: Int, totalCount: Int, message: String): LoadingUiModel {
        return LoadingUiModel(currentCount, totalCount, message)
    }

}