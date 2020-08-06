package com.mutakin.githubfinder.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.AVATAR_URL
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.USERNAME
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "dbfavoriteuser"
        private const val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORITE_USER = """
            CREATE TABLE $TABLE_NAME
            ($_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                 $USERNAME TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,
                 $AVATAR_URL TEXT NOT NULL)
        """.trimIndent()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}