package com.akshay.todoapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, TABLE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL1 TEXT UNIQUE,$COL2 BOOLEAN)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(item: String){
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL1, item)
        contentValues.put(COL2, false)
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    val data: Cursor
        get() {
            val db = this.writableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            return db.rawQuery(query, null)
        }

    fun updateName(name: String) {
        val db = this.writableDatabase
        val query = ("UPDATE $TABLE_NAME SET $COL2=1 WHERE $COL1='$name'")
        db.execSQL(query)
    }

    fun deleteName(data: String) {
        val db = this.writableDatabase
        val query = ("DELETE FROM $TABLE_NAME WHERE content='$data'")
        db.execSQL(query)
    }

    companion object {
        private const val TABLE_NAME = "tasks"
        //column 0 will be the id of each task... or use ID for comparing ID of the task
        private const val COL1 = "content"
        private const val COL2 = "completed"
    }
}