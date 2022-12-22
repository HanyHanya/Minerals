package com.example.minerals.data

import androidx.lifecycle.LiveData

class MineralsRepository(private val mineralsDao: MineralDao) {
    val getAllMinerals: LiveData<List<Mineral>> = mineralsDao.getAllMinerals()

    suspend fun addMineral(mineral: Mineral) {
        mineralsDao.addMineral(mineral)
    }

    suspend fun updateMineral(mineral: Mineral) {
        mineralsDao.updateMineral(mineral)
    }

    suspend fun deleteMineral(mineral: Mineral) {
        mineralsDao.deleteMineral(mineral)
    }

    fun nukeTable() {
        mineralsDao.nukeTable()
    }
}