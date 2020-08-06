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
import com.mutakin.githubfinder.adapter.FollowingAdapter
import com.mutakin.githubfinder.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingProgressBar.visibility = View.VISIBLE
        val username =
            activity?.intent?.getStringExtra(DetailUserActivity.EXTRA_USERNAME).toString()

        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)
        followingViewModel.setFollowing(username)
        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItem ->
            if (followingItem != null) {
                followingAdapter.setData(followingItem)
                followingProgressBar.visibility = View.GONE
            }
        })

        showRecyclerView()
    }


    private fun showRecyclerView() {
        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()

        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = followingAdapter
        rv_following.setHasFixedSize(true)
    }
}