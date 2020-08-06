package com.mutakin.githubfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mutakin.githubfinder.model.User
import com.mutakin.githubfinder.rest.APIService
import com.mutakin.githubfinder.rest.RestClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setFollowing(username: String) {
        val mAPIService: APIService = RestClient.client.create(APIService::class.java)
        val call = mAPIService.getDetailFollow(username, "following")
        call.enqueue(object : Callback, retrofit2.Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val following = response.body()
                listFollowing.postValue(following)
            }

        })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}