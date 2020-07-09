package com.google.firebase.samples.apps.mlkit.translate

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class DataSaveHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,DATABASE_VERSION){
    companion object{
        private val DATABASE_NAME = "savednotes.db"
        private val DATABASE_VERSION = 3
        val TABLE_NAME = "NOTES"
        val KEY_NAME = "name"
        val KEY_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + "TIME" + " INTEGER PRIMARY KEY," +
                "NAME" + " TEXT)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME)
        onCreate(db)
    }

    fun addNotes(notes: Notes):Boolean{
        var db = this.writableDatabase
        var values = ContentValues()
        values.put(KEY_NAME,notes.name)
        values.put(KEY_TIME,notes.time)
        val success = db.insert(TABLE_NAME,null,values)
        db.close()
        if (success.toInt() == -1){
            Toast.makeText(context,"Data note inserted successfully",Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            Toast.makeText(context,"Data inserted",Toast.LENGTH_SHORT).show()
            return true
        }
    }

    fun deleteNotes(notes: Notes){
        var db = this.writableDatabase
        val selectionArgs = arrayOf(notes.time.toString())
        db.delete(TABLE_NAME,KEY_TIME+" = ? ",selectionArgs)
    }

    fun getAllNotes():ArrayList<Notes>{
        var db = this.writableDatabase
        var notesList : ArrayList<Notes> = ArrayList()
        val selectAll = "SELECT * FROM "+ TABLE_NAME
        val cursor = db.rawQuery(selectAll,null)
        if (cursor.moveToFirst()){
            do{
                val notes = Notes()
                notes.time = cursor.getInt(0)
                notes.name = cursor.getString(1)
                notesList.add(notes)
            } while (cursor.moveToNext())
        }
        return notesList
    }

}