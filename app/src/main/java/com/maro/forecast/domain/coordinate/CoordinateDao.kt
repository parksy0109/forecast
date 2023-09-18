package com.maro.forecast.domain.coordinate

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoordinateDao {

    @Insert
    fun insert(coordinate: Coordinate)

    @Query("SELECT * FROM Coordinate WHERE firstStep = :firstStep LIMIT 1")
    fun findByStep(firstStep: String): Coordinate

    @Query("SELECT * FROM Coordinate WHERE firstStep = :firstStep AND secondStep = :secondStep LIMIT 1")
    fun findByStep(firstStep: String, secondStep: String): Coordinate

    @Query("SELECT * FROM Coordinate WHERE firstStep = :firstStep AND secondStep = :secondStep AND thirdStep = :thirdStep LIMIT 1")
    fun findByStep(firstStep: String, secondStep: String, thirdStep: String): Coordinate

    @Query("SELECT COUNT(*) FROM Coordinate")
    fun getCount(): Int

}