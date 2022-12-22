package com.example.minerals.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE

@Dao
interface MineralDao {
    @Insert(onConflict = IGNORE)
    suspend fun addMineral(mineral: Mineral)

    @Query("SELECT * FROM minerals ORDER BY id ASC")
    fun getAllMinerals(): LiveData<List<Mineral>>

    @Update
    suspend fun updateMineral(mineral: Mineral)

    @Delete
    suspend fun deleteMineral(mineral: Mineral)

    @Query("DELETE FROM minerals")
    fun nukeTable()
}