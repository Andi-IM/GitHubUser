package live.andiirham.githubuser.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import live.andiirham.githubuser.R
import live.andiirham.githubuser.model.User
import live.andiirham.githubuser.view.detail.DetailActivity
import live.andiirham.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private val list = ArrayList<User>()
        private lateinit var mainViewModel: MainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = resources.getString(R.string.home)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        showList()
    }

    // When Options Created
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari User"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // if submitted
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()

                // memanggil kueri
                if (query.isEmpty()) {
                    showLoading(false)
                    return false
                }

                mainViewModel.setUser(query)
                //Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            // if changed
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    // Language settings
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return true
    }

    // Showing User
    private fun showList() {
        rv_users.setHasFixedSize(true)
        rv_users.layoutManager = LinearLayoutManager(this)

        val userAdapter = UserAdapter(list)
        userAdapter.notifyDataSetChanged()

        rv_users.adapter = userAdapter

        mainViewModel.getUser().observe(this, Observer { userItems ->
            if (userItems != null) {
                userAdapter.setData(userItems)
                showLoading(false)
            } else {
                showLoading(false)
                Toast.makeText(this, "Something Error", Toast.LENGTH_LONG).show()
            }
        })

        userAdapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    // For Intent Showing User
    private fun showSelectedUser(user: User) {
        val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_URL, user.url)
        detailIntent.putExtra(DetailActivity.EXTRA_USERNAME, user.username)
        startActivity(detailIntent)
    }

    // Loading Show when query submitted
    private fun showLoading(state: Boolean) {
        if (state) {
            welcome.visibility = View.GONE
            rv_users.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            rv_users.visibility = View.VISIBLE
        }
    }
}