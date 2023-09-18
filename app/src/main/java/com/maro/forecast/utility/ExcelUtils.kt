package com.maro.forecast.utility

import android.content.Context
import android.util.Log
import com.maro.forecast.data.LocationClassification
import com.maro.forecast.domain.coordinate.Coordinate
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ExcelUtils {

    fun readCoordinateData(context: Context): List<Coordinate> {
        val result = mutableListOf<Coordinate>()

        try {
            val assetManager = context.assets
            val inputStream = assetManager.open("coordinate_data.xlsx")
            val xssfWorkbook = XSSFWorkbook(inputStream)

            val sheet = xssfWorkbook.getSheetAt(0)

            val lastRowNum = sheet.lastRowNum


            for (i in 2 until lastRowNum) {
                val row = sheet.getRow(i)
                result.add(
                    Coordinate(
                        firstStep = row.getCell(2).toString(),
                        secondStep = row.getCell(3).toString(),
                        thirdStep = try {
                            row.getCell(4).toString()
                        } catch (e: Exception) {
                            null
                        },
                        x = row.getCell(5).toString().toInt(),
                        y = row.getCell(6).toString().toInt(),
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(context.toString(), e.message.toString())
        }

        return result
    }

    fun getLocationClassification(context: Context): Map<String, List<LocationClassification>> {
        val result = mutableListOf<LocationClassification>()

        try {
            val assetManager = context.assets
            val inputStream = assetManager.open("coordinate_data.xlsx")
            val xssfWorkbook = XSSFWorkbook(inputStream)

            val sheet = xssfWorkbook.getSheetAt(0)

            val lastRowNum = sheet.lastRowNum


            for (i in 2 until lastRowNum) {
                val row = sheet.getRow(i)
                result.add(
                    LocationClassification(
                        firstStep = row.getCell(2).toString(),
                        secondStep = row.getCell(3).toString(),
                        thirdStep = try {
                            row.getCell(4).toString()
                        } catch (e: Exception) {
                            null
                        },
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(context.toString(), e.message.toString())
        }

        return result.groupBy { it.firstStep }
    }

}