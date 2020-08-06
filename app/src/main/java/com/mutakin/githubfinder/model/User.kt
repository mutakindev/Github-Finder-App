package com.mutakin.githubfinder.model

import com.google.gson.annotations.SerializedName

data class User(

    @field:SerializedName("login")
    val username: String,

    @field:SerializedName("company")
    val company: String,

    @field:SerializedName("public_repos")
    val repository: Int,


    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatar: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("location")
    val location: String

    )
