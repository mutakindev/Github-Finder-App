package com.mutakin.consumerapp

import android.database.ContentObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mutakin.consumerapp.adapter.FavoriteUserAdapter
import com.mutakin.consumerapp.database.DatabaseContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.mutakin.consumerapp.helper.MappingHelper
import com.mutakin.consumerapp.model.FavoriteUser
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var contentObserver: ContentObserver

    companion object {
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        title = getString(R.string.app_name)
        progressBar.isVisible = true

        rv_userFavorite.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteUserAdapter()
        rv_userFavorite.adapter = adapter
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(rv_userFavorite)
        rv_userFavorite.setHasFixedSize(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        contentObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadDataFavoriteUser()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, contentObserver)

        if (savedInstanceState != null) {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
            progressBar.isVisible = false
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
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progressBar.isVisible = false

            val favoriteUser = data.await()
            if (favoriteUser.size > 0) {
                adapter.listFavoriteUser = favoriteUser
            } else {
                adapter.listFavoriteUser = ArrayList()
                showSnackbarMessage(getString(R.string.data_not_found))
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_userFavorite, message, Snackbar.LENGTH_SHORT).show()
    }


    private var swipeToDeleteCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            private val colorBackground: ColorDrawable = ColorDrawable(Color.RED)

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20;
//
//                val iconMargin = (itemView.height - icon?.intrinsicHeight as Int) / 2
//                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
//                val iconBottom = iconTop + icon.intrinsicHeight


                when {
                    dX > 0 -> {
//                        val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
//                        val iconRight = itemView.left + iconMargin
//                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        colorBackground.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset,
                            itemView.bottom
                        )
                    }
                    dX < 0 -> {
//                        val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
//                        val iconRight = itemView.right - iconMargin
//                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        colorBackground.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }
                    else -> {
                        colorBackground.setBounds(0, 0, 0, 0)
                    }
                }

                colorBackground.draw(c)
//                icon.draw(c)
            }

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
                    val uriWithId = Uri.parse("$CONTENT_URI/$id")
                    val result = contentResolver.delete(uriWithId, null, null)
                    if (result > 0) {
                        showSnackbarMessage(getString(R.string.delete_data_info_text))
                        adapter.removeItem(viewHolder.adapterPosition)
                    } else {
                        showSnackbarMessage(getString(R.string.delete_data_error_text))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
    }
}