package com.mutakin.githubfinder.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteUser(
    var id: Int = 0,
    var username: String? = null,
    var avatarUrl: String? = null
) : Parcelable