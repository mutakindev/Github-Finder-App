package com.mutakin.githubfinder.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.helper.CustomOnItemClickListener
import com.mutakin.githubfinder.model.FavoriteUser
import com.mutakin.githubfinder.view.DetailUserActivity
import kotlinx.android.synthetic.main.item_layout.view.*

class FavoriteUserAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {

    var listFavoriteUser = ArrayList<FavoriteUser>()
        set(listFavoriteUser) {
            if (listFavoriteUser.size > 0){
                this.listFavoriteUser.clear()
            }
            this.listFavoriteUser.addAll(listFavoriteUser)
            notifyDataSetChanged()
        }

    fun getItemByPosition(position: Int) = this.listFavoriteUser[position]

    fun removeItem(position: Int) {
        this.listFavoriteUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavoriteUser.size)
    }


    inner class FavoriteUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoriteUser: FavoriteUser) {
            with(itemView){
                Glide
                    .with(this)
                    .load(favoriteUser.avatarUrl)
                    .centerCrop()
                    .into(avatar)
                tvUsername.text = favoriteUser.username
                this.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, DetailUserActivity::class.java)
                        intent.putExtra(DetailUserActivity.EXTRA_USERNAME, favoriteUser.username)
                        activity.startActivity(intent)
                    }

                }))
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserAdapter.FavoriteUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return FavoriteUserViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavoriteUser.size

    override fun onBindViewHolder(holder: FavoriteUserAdapter.FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }
}