package com.example.alaaismail.notesappdemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class dbManger {

    val dbName = "MyNotes"
    val dbTable = "Notes"
    val ColID = "ID"
    val ColTitle = "Title"
    val ColDesc = "Description"
    val dbVersion = 1

    val sqlCreatTable =
        "CREATE TABLE IF NOT EXISTS  $dbTable ($ColID INTEGER PRIMARY KEY, $ColTitle TEXT, $ColDesc TEXT);"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context) {

        var db = DataBaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DataBaseHelperNotes : SQLiteOpenHelper {
        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreatTable)
            Toast.makeText(this.context, "dataBase is created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS $dbTable")
        }

    }

    fun Insert(value: ContentValues): Long {

        val ID = sqlDB!!.insert(dbTable, "", value)
        return ID
    }

    fun Query (projection:Array<String>, selection:String , selectionArgs:Array<String>,sortOrder:String): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

    fun Delete(selection:String , selectionArgs:Array<String>):Int{
        val count =sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count

    }

    fun Update (value: ContentValues,selection:String , selectionArgs:Array<String>):Int{

        val count = sqlDB!!.update(dbTable,value,selection,selectionArgs)
        return count
    }

}