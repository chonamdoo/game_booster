package com.couchgram.gamebooster.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by chonamdoo on 2017. 4. 27..
 */

class BoosterDbHelper(context : Context) : SQLiteOpenHelper(context,BoosterDbHelper.DATABASE_NAME,null,BoosterDbHelper.DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object{
        val DATABASE_NAME = "Booster.db"
        val DATABASE_VERSION = 1
        val TABLE_NAME ="game_booster"
        val _ID = "_id"
        val COLUMN_NAME_APP_NAME ="app_name"
        val COLUMN_NAME_APP_PACKAGE_NAME ="app_package_name"
        val COLUMN_NAME_ADD_TIME ="add_time"
        val COLUMN_NAME_ADD_POSITION ="add_position"
        val CREATE_SQL = "CREATE TABLE " + TABLE_NAME + "( " +
                _ID +" integer primary key autoincrement , "+
                COLUMN_NAME_APP_NAME +" TEXT NOT NULL ,"+
                COLUMN_NAME_APP_PACKAGE_NAME+" TEXT NOT NULL ,"+
                COLUMN_NAME_ADD_TIME + " TEXT NOT NULL ,"+
                COLUMN_NAME_ADD_POSITION + " integer default 0)"

    }
}