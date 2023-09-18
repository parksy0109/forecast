package com.maro.forecast.retrofit

import android.content.Context
import com.maro.forecast.domain.coordinate.Coordinate
import com.maro.forecast.retrofit.response.UltraSrtNcst
import com.maro.forecast.utility.TimeUtils
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

class PublicDataAPIRepository {

    suspend fun getCurrentUltraSrtNcst(baseDate: String, baseTime: String, coordinate: Coordinate): Response<UltraSrtNcst> {
        return RetrofitInstance().api.getUltraSrtNcst(
            baseDate = baseDate,
            baseTime = baseTime,
            nx = coordinate.x,
            ny = coordinate.y
        )
    }

}