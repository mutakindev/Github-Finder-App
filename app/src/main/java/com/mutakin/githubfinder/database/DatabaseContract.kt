package com.mutakin.githubfinder.database

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteUserColumns: BaseColumns{
        companion object{
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR_URL = "avatarUrl"
        }
    }
}