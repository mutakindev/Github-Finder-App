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

class DetailUserViewModel : ViewModel() {
    val detailUser = MutableLiveData<User>()
    fun setDetailUser(username: String) {
        val mAPIService: APIService = RestClient.client.create(APIService::class.java)
        val call = mAPIService.getDetailUser(username)
        call.enqueue(object : Callback, retrofit2.Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if (user != null) {
                    detailUser.postValue(user)
                }
            }

        })
    }


    fun getDetailUser(): LiveData<User> {
        return detailUser
    }
}