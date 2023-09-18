package com.maro.forecast.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maro.forecast.domain.coordinate.Coordinate
import com.maro.forecast.domain.coordinate.CoordinateDao

@Database(entities = [Coordinate::class], version = 1)
abstract class ForecastDatabase: RoomDatabase() {
    abstract fun coordinateDao(): CoordinateDao

    companion object {
        private var instance: ForecastDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ForecastDatabase? {
            if(instance == null) {
                synchronized(ForecastDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ForecastDatabase::class.java,
                        "forecast-database"
                    ).build()
                }
            }

            return instance
        }
    }
}