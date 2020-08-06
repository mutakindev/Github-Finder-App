package com.mutakin.githubfinder.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.adapter.FollowersAdapter
import com.mutakin.githubfinder.viewmodel.FollowersViewModel
import kotlinx.android.synthetic.main.fragment_followers.*


class FollowersFragment : Fragment() {
    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerProgressBar.visibility = View.VISIBLE

        val username =
            activity?.intent?.getStringExtra(DetailUserActivity.EXTRA_USERNAME).toString()

        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        followersViewModel.setFollowers(username)

        followersViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followerItem ->
            if (followerItem != null) {
                followersAdapter.setData(followerItem)
                followerProgressBar.visibility = View.GONE
            }
        })

        showRecyclerList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }


    private fun showRecyclerList() {
        followersAdapter = FollowersAdapter()
        followersAdapter.notifyDataSetChanged()

        rv_followers.layoutManager = LinearLayoutManager(activity)
        rv_followers.adapter = followersAdapter
        rv_followers.setHasFixedSize(true)
    }


}