package com.mutakin.githubfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.model.User
import kotlinx.android.synthetic.main.item_layout.view.*
import java.util.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>(){
    private val listFollowers =  ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        listFollowers.clear()
        listFollowers.addAll(items)
        notifyDataSetChanged()
    }

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return FollowersViewHolder(mView)
    }

    override fun getItemCount(): Int = listFollowers.size


    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowers[position])
    }

}