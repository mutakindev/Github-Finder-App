package com.mutakin.githubfinder.view

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.adapter.SectionsPagerAdapter
import com.mutakin.githubfinder.database.DatabaseContract
import com.mutakin.githubfinder.database.FavoriteUserHelper
import com.mutakin.githubfinder.helper.MappingHelper
import com.mutakin.githubfinder.viewmodel.DetailUserViewModel
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var favoriteUserHelper: FavoriteUserHelper
    private lateinit var values: ContentValues
    private var statusFavorite: Boolean = false

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        supportActionBar?.elevation = 0F
        title = getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)

        showLoading(true)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username != null) {
            initDetailUserViewModel(username)
            queryByUsername(username)
        }


        btn_add_favorite.setOnClickListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initDetailUserViewModel(username: String?) {
        detailUserViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)

        if (username != null) {
            detailUserViewModel.setDetailUser(username)
        }

        detailUserViewModel.getDetailUser().observe(this, Observer { userItem ->
            if (userItem != null) {
                Glide
                    .with(this)
                    .load(userItem.avatar)
                    .fitCenter()
                    .into(imgAvatar)
                txt_detail_name.text = userItem.name
                txt_detail_username.text = userItem.username
                txt_detail_company.text = userItem.company
                txt_detail_location.text = userItem.location
                txt_detail_repository.text = userItem.repository.toString()
                txt_detail_followers.text = userItem.followers.toString()
                txt_detail_following.text = userItem.following.toString()



                values = ContentValues()
                values.put(DatabaseContract.FavoriteUserColumns.USERNAME, userItem.username)
                values.put(DatabaseContract.FavoriteUserColumns.AVATAR_URL, userItem.avatar)


                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        detailProgressBar.isVisible = state
    }

    private fun queryByUsername(username: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val data = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryByUsername(username)
                MappingHelper.mapCursorToObject(cursor)
            }


            val oneFavoriteUser = data.await()
            if (oneFavoriteUser.username != null) {
                statusFavorite = true
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {

        if (statusFavorite) {
            btn_add_favorite.setImageDrawable(resources.getDrawable(R.drawable.favorite_fill))
        } else {
            btn_add_favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_favorite_border_24))
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_favorite -> {
                try {
                    val result = favoriteUserHelper.insert(values)
                    if (result > 0) {
                        statusFavorite = true
                        setStatusFavorite(statusFavorite)
                    } else {
                        Toast.makeText(
                            this@DetailUserActivity,
                            "Gagal Menambah Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

}