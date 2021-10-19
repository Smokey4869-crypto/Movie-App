package com.example.myapplication.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavDB(context: Context, factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "FavoriteMoviesDB"
        const val DATABASE_VERSION = 1
        const val ID_COL = "movie_id"
        const val NAME_COL = "movie_name"
        const val DATE_COL = "release_date"
    }
    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun createNewList(db: SQLiteDatabase?, listName: String ) {
        if (tableExists(db, listName)) {
            val query = ("CREATE TABLE " + listName + "(" +
                    ID_COL + " INTEGER PRIMARY KEY, " +
                    NAME_COL + " TEXT, " +
                    DATE_COL + " TEXT)")
            db?.execSQL(query)
        } else {
        }
    }

    private fun tableExists(db: SQLiteDatabase?, table: String): Boolean {
        var count = 0
        if (db == null || !db.isOpen)
            return false
        val args: Array<String> = arrayOf("table", table)
        val cursor: Cursor = db.rawQuery("SELECT COUNT(*) FROM ${DATABASE_NAME} WHERE type=? AND name=?", args)
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count > 0
    }
 }