package com.maro.forecast.domain.coordinate

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Coordinate(
    var firstStep: String,
    var secondStep: String?,
    var thirdStep: String?,
    var x: Int,
    var y: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}