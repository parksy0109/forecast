package com.maro.forecast.utility

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {

    fun getBaseTime(localDateTime: LocalDateTime): String {

        val minute = localDateTime.minute
        val hour = localDateTime.hour

        return if (minute > 40) {
            if (hour > 10) {
                "${hour}00"
            }else {
                "0${hour}00"
            }
        }else {
            val hourMinusOne = hour.minus(1)
            if (hourMinusOne > 10) {
                "${hourMinusOne}00"
            }else {
                "0${hourMinusOne}00"
            }
        }
    }

    fun getBaseDate(localDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return localDate.format(formatter)
    }

}