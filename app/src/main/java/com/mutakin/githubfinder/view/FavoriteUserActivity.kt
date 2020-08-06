package com.mutakin.githubfinder.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.adapter.FavoriteUserAdapter
import com.mutakin.githubfinder.database.FavoriteUserHelper
import com.mutakin.githubfinder.helper.MappingHelper
import com.mutakin.githubfinder.model.FavoriteUser
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        title = getString(R.string.user_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressBar.isVisible = true

        rv_userFavorite.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteUserAdapter(this)
        rv_userFavorite.adapter = adapter
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_userFavorite)
        rv_userFavorite.setHasFixedSize(true)

        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        if (savedInstanceState != null) {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
        } else {
            loadDataFavoriteUser()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavoriteUser)
    }

    private fun loadDataFavoriteUser() {
        GlobalScope.launch(Dispatchers.Main) {
            val data = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progressBar.isVisible = false

            val favoriteUser = data.await()
            if (favoriteUser.size > 0) {
                adapter.listFavoriteUser = favoriteUser
            } else {
                adapter.listFavoriteUser = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_userFavorite, message, Snackbar.LENGTH_SHORT).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    val id = adapter.getItemByPosition(viewHolder.adapterPosition).id.toString()
                    val result = favoriteUserHelper.deleteById(id)
                    if (result > 0) {
                        showSnackbarMessage("Delete data Success")
                        adapter.removeItem(viewHolder.adapterPosition)
                    } else {
                        showSnackbarMessage("Delete data failed")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }


    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
    }
}