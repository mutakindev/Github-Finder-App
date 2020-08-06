package com.mutakin.githubfinder.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mutakin.githubfinder.model.GithubResponse
import com.mutakin.githubfinder.model.User
import com.mutakin.githubfinder.rest.APIService
import com.mutakin.githubfinder.rest.RestClient
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserViewModel : ViewModel() {
    private var onErrorAction: ShowErrorMessage? = null
    fun setOnErrorAction(onErrorAction: ShowErrorMessage) {
        this.onErrorAction = onErrorAction
    }

    private val listUsers = MutableLiveData<ArrayList<User>>()
    fun setUsers(username: String) {
        val mAPIService: APIService = RestClient.client.create(APIService::class.java)
        val call = mAPIService.getUsers(username)
        call.enqueue(object : Callback, retrofit2.Callback<GithubResponse> {
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                onErrorAction?.onFailedLoadUser(t.message.toString())
            }

            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                val users = response.body()
                if (users != null) {
                    listUsers.postValue(users.items)
                }
            }

        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }

    interface ShowErrorMessage {
        fun onFailedLoadUser(errorMessage: String)
    }
}