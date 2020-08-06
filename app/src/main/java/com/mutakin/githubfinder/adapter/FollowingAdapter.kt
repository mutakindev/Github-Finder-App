package com.mutakin.githubfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.model.User
import kotlinx.android.synthetic.main.item_layout.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>(){
    private val listFollowing =  ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listFollowing.clear()
        listFollowing.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide
                    .with(this)
                    .load(user.avatar)
                    .centerCrop()
                    .into(avatar)
                tvUsername.text = user.username
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return FollowingViewHolder(mView)
    }

    override fun getItemCount(): Int = listFollowing.size


    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowing[position])
    }

}