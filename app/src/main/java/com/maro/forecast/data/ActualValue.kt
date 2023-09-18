package com.maro.forecast.data

enum class ActualValue(val itemName: String, val unit: String) {
    T1H("기온", "℃"),
    RN1("1시간 강수량", "mm"),
    UUU("동서바람성분", "m/s"),
    VVV("남북바람성분", "m/s"),
    REH("습도", "%"),
    PTY("강수형태", "코드값"), // (초단기) 없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
    VEC("풍향", "deg"),
    WSD("풍속", "m/s");

    companion object {
        fun findByActualValue(actualValue: String): ActualValue? {
            val dataSet: MutableMap<String, ActualValue> = mutableMapOf()
            ActualValue.values().forEach { dataSet[it.name] = it }
            return dataSet[actualValue]
        }
    }

}