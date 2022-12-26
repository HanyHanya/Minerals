package com.example.minerals.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

val DATABASE_NAME = "MINERALS_DATABASE"

@Database(entities = [Mineral::class], version = 1, exportSchema = true)
abstract class MineralsDatabase: RoomDatabase() {

    abstract fun minrealsDao(): MineralDao

    companion object {
        @Volatile
        private var INSTANCE: MineralsDatabase? = null

        fun getDatabase(context: Context): MineralsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MineralsDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}