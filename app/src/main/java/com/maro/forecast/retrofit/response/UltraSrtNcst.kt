package com.maro.forecast.retrofit.response


import com.google.gson.annotations.SerializedName

data class UltraSrtNcst(
    @SerializedName("response")
    val response: Response
) {
    data class Response(
        @SerializedName("body")
        val body: Body,
        @SerializedName("header")
        val header: Header
    ) {
        data class Body(
            @SerializedName("dataType")
            val dataType: String,
            @SerializedName("items")
            val items: Items,
            @SerializedName("numOfRows")
            val numOfRows: Int,
            @SerializedName("pageNo")
            val pageNo: Int,
            @SerializedName("totalCount")
            val totalCount: Int
        ) {
            data class Items(
                @SerializedName("item")
                val item: List<Item>
            ) {
                data class Item(
                    @SerializedName("baseDate")
                    val baseDate: String,
                    @SerializedName("baseTime")
                    val baseTime: String,
                    @SerializedName("category")
                    val category: String,
                    @SerializedName("nx")
                    val nx: Int,
                    @SerializedName("ny")
                    val ny: Int,
                    @SerializedName("obsrValue")
                    val obsrValue: String
                )
            }
        }

        data class Header(
            @SerializedName("resultCode")
            val resultCode: String,
            @SerializedName("resultMsg")
            val resultMsg: String
        )
    }
}