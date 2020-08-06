package com.mutakin.githubfinder.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.TABLE_NAME
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.USERNAME
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion._ID
import java.sql.SQLException

class FavoriteUserHelper(context: Context) {
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE: FavoriteUserHelper? = null

        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): FavoriteUserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteUserHelper(context)
            }

    }

    init {
        databaseHelper = DatabaseHelper(context)
    }


    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    fun queryByUsername(username: String): Cursor? {
        return database.query(
            DATABASE_TABLE,
            null,
            "$USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

}