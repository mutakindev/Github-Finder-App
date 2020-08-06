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

class FollowersViewModel : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setFollowers(username: String) {
        val mAPIService : APIService = RestClient.client.create(APIService::class.java)
        val call = mAPIService.getDetailFollow(username,"followers")
        call.enqueue(object : Callback, retrofit2.Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val followers = response.body()
                listFollowers.postValue(followers)
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}