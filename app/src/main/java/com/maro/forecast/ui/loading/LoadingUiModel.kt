package com.maro.forecast.ui.loading

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class LoadingUiModel(
    var currentCount: Int,
    var totalCount: Int,
    val currentMessage: String?
)

class SampleLoadingUiModel: PreviewParameterProvider<LoadingUiModel> {
    override val values: Sequence<LoadingUiModel>
        get() = sequenceOf(LoadingUiModel(3, 256, null))
}