package com.maro.forecast.data

enum class PTYValue(val code: Int, val description: String) {
    NO(0, "없음"),
    RAIN(1, "비"),
    RAINSNOW(2, "비/눈"),
    SNOW(3, "눈"),
    RAINDROP(5, "빗방울"),
    RAINDROPSNOW(6, "빗방울눈날림"),
    SNOWDROP(7, "눈날림");

    companion object {
        fun findByCode(code: Int): PTYValue? {
            val dataSet = mutableMapOf<Int, PTYValue>()
            PTYValue.values().forEach { dataSet[it.code] = it }
            return dataSet[code]
        }
    }
}

// (초단기) 없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)