package com.mutakin.githubfinder.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mutakin.githubfinder.R
import com.mutakin.githubfinder.adapter.UserAdapter
import com.mutakin.githubfinder.model.User
import com.mutakin.githubfinder.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var userAdapter: UserAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(UserViewModel::class.java)

        userViewModel.getUser().observe(this, Observer { userItem ->
            if (userItem != null) {
                userAdapter.setData(userItem)
                showLoading(false)
            }
        })

        searchViewManager()
        showRecyclerList()
        hideKeyboard()
    }

    private fun showLoading(state: Boolean) {
        progressBar.isVisible = state
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun showRecyclerList() {
        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = userAdapter
        rv_users.setHasFixedSize(true)

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                onSelectedUser(data)
            }
        })
    }

    private fun onSelectedUser(user: User) {
        Intent(this@MainActivity, DetailUserActivity::class.java).also {
            it.putExtra(DetailUserActivity.EXTRA_USERNAME, user.username)
            startActivity(it)
        }

    }

    private fun searchViewManager() {

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.search_user)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                if (query.isEmpty()) return false
                showLoading(true)

                userViewModel.setUsers(query)

               userViewModel.setOnErrorAction(object : UserViewModel.ShowErrorMessage {
                   override fun onFailedLoadUser(errorMessage: String) {
                       Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                       showLoading(false)
                   }

               })
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting -> Intent(this@MainActivity, SettingsActivity::class.java).also {
                startActivity(it)
            }
            R.id.favoriteUser -> {
                Intent(this@MainActivity, FavoriteUserActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return true
    }
}