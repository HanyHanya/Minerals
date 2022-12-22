package com.example.minerals.Repositories

import com.example.minerals.Mineral

interface IRepository {
    fun getMineral(): ArrayList<Mineral>
    fun updateMineral(updatedCharacter: Mineral)
    fun deleteMineral(id: String)
    fun getMineral(id: String): Mineral
    fun saveMineral(Character: Mineral)
}