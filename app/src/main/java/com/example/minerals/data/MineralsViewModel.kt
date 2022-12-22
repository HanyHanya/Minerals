package com.example.minerals.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MineralsViewModel(application: Application): AndroidViewModel(application) {
    val getAllMinerals: LiveData<List<Mineral>>
    private val repository: MineralsRepository

    init {
        val mineralsDao = MineralsDatabase.getDatabase(application).minrealsDao()
        repository = MineralsRepository(mineralsDao)
        getAllMinerals = repository.getAllMinerals
    }

    fun addMineral(mineral: Mineral) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMineral(mineral)
        }
    }

    fun updateMineral(mineral: Mineral) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMineral(mineral)
        }
    }

    fun deleteMineral(mineral: Mineral) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMineral(mineral)
        }
    }

    fun nukeTable() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.nukeTable()
        }
    }
}