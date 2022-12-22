package com.example.minerals.Repositories

import android.annotation.SuppressLint
import android.content.ContentValues
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
        var note: String?
        var type: String?

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(COL_ID))
                image = Uri.parse(cursor.getString(cursor.getColumnIndex(COL_IMAGE)))
                name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                type = cursor.getString(cursor.getColumnIndex(COL_NAME))
                note = cursor.getString(cursor.getColumnIndex(COL_NAME))

                val Mineral = Mineral(image, id, name, type, note)
                MineralsList.add(Mineral)
            } while (cursor.moveToNext())
        }

        return MineralsList
    }

    override fun getMineral(id: String) : Mineral {
        val Mineral = getMineral()
        return Mineral.filter { Mineral -> Mineral.id == id }[0]
    }

    override fun deleteMineral(id: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_ID, id)

        val success = db.delete(TABLE_NAME, "id=\"" + id + "\"", null)

        db.close()
    }

    override fun saveMineral(Mineral: Mineral) {
        val db = this.writableDatabase

        var contentValues = putContentValues(Mineral)

        val success = db.insertOrThrow(TABLE_NAME, null, contentValues)

        db.close()
    }

    private fun putContentValues(Mineral: Mineral): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(COL_ID, Mineral.id)
        contentValues.put(COL_IMAGE, Mineral.image.toString())
        contentValues.put(COL_NAME, Mineral.name)
        contentValues.put(COL_TYPE, Mineral.type)
        contentValues.put(COL_NOTE, Mineral.note)

        return contentValues
    }

    override fun updateMineral(updatedMineral: Mineral) {
        deleteMineral(updatedMineral.id)
        saveMineral(updatedMineral)
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " TEXT," +
                COL_IMAGE + " TEXT," +
                COL_NAME + " TEXT," +
                COL_TYPE + " TEXT," +
                COL_NOTE + " TEXT" +
                ")")

        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(p0)
    }
}