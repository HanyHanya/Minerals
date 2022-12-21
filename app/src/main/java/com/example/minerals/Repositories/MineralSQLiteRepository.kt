package com.example.minerals.Repositories

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.example.minerals.Mineral

val DATABASE_NAME="Minereals"
val TABLE_NAME="Mineral"
val COL_ID="id"
val COL_IMAGE="image"
val COL_NAME="name"
val COL_TYPE="type"
val COL_NOTE="note"

class MineralSQLiteRepository (context: Context): IRepository, SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    @SuppressLint("Range")

    override fun getMineral(): ArrayList<Mineral> {
        val MineralsList: ArrayList<Mineral> = ArrayList()

        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: String
        var image: Uri?
        var name: String
        var age: Int?
        var note: String?
        var type: String?

        return MineralsList
    }

    override fun getMineral(id: String) : Mineral {
        TODO("Not yet implemented")
    }

    override fun deleteMineral(id: String) {
        TODO("Not yet implemented")
    }

    override fun saveMineral(Mineral: Mineral) {
        TODO("Not yet implemented")
    }

    override fun updateMineral(updatedMineral: Mineral) {
        TODO("Not yet implemented")
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " TEXT," +
                COL_IMAGE + " TEXT," +
                COL_NAME + " TEXT," +
                COL_TYPE + " TEXT," +
                COL_NOTE + " TEXT," +
                ")")

        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(p0)
    }
}