package com.maro.forecast.retrofit

import com.maro.forecast.retrofit.response.UltraSrtNcst
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PublicDataAPI {
    @GET("/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst")
    suspend fun getUltraSrtNcst(
        @Query("serviceKey") serviceKey: String = SERVICE_KEY,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("pageNo") pageNo: Int = 1,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ) : Response<UltraSrtNcst>

    companion object {
        const val SERVICE_KEY = "your_public_data_api_key"
    }
}