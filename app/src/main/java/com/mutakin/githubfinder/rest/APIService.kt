package com.mutakin.githubfinder.rest

import com.mutakin.githubfinder.model.GithubResponse
import com.mutakin.githubfinder.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/search/users")
    fun getUsers( @Query("q") username: String): Call<GithubResponse>

    @GET("/users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<User>

    @GET("/users/{username}/{key}")
    fun getDetailFollow(@Path("username") username: String,@Path("key") key: String) : Call<ArrayList<User>>
}