package com.mutakin.githubfinder.helper

import android.database.Cursor
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.AVATAR_URL
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion.USERNAME
import com.mutakin.githubfinder.database.DatabaseContract.FavoriteUserColumns.Companion._ID
import com.mutakin.githubfinder.model.FavoriteUser

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<FavoriteUser> {
        val listFavoriteUser = ArrayList<FavoriteUser>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                listFavoriteUser.add(FavoriteUser(id, username, avatarUrl))
            }
        }
        return listFavoriteUser
    }

    fun mapCursorToObject(cursor: Cursor?): FavoriteUser {
        var favoriteUser = FavoriteUser()
        cursor?.apply {
            while (cursor.moveToFirst()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(AVATAR_URL))
                favoriteUser = FavoriteUser(id, username, avatarUrl)
            }


        }
        return favoriteUser
    }
}