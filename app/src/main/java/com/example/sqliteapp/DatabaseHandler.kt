package com.example.sqliteapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Users"
val COL_NAME = "name"
val COL_AGE = "age"
val COL_ID = "id"

class DatabaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(db: SQLiteDatabase?) {
        //When running on device for the first time
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_AGE + " INTEGER)";
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //When older version of database present

    }

    fun insertData(user: User){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME,user.name)
        cv.put(COL_AGE, user.age)

        val result = db.insert(TABLE_NAME, null, cv)

        if (result==-1.toLong()){
            Toast.makeText(context,"Failed!!",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Success!!",Toast.LENGTH_SHORT).show()
        }
    }

    fun readData() : MutableList<User>{
        var list: MutableList<User>
        list = ArrayList()

        var db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)

        //if result is not null, has atleast 1 value = returns true
        if (result.moveToFirst()){
            do {
                var user = User()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
                list.add(user)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData(){
        var db = this.writableDatabase

        //Only record delete which have id: 1
        db.delete(TABLE_NAME, COL_ID + "=?", arrayOf(1.toString()))
        //All record delete
        //db.delete(TABLE_NAME, null, null))
        db.close()
    }

    fun updateData(){
        var db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)

        //if result is not null, has atleast 1 value = returns true
        if (result.moveToFirst()){
            do {
                var cv = ContentValues()
                //incrementing age by 1
                cv.put(COL_AGE, (result.getInt(result.getColumnIndex(COL_AGE))+1))
                db.update(TABLE_NAME, cv, COL_ID + "=? AND " + COL_NAME + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_NAME))))
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }

}