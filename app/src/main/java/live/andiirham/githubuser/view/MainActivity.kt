package live.andiirham.githubuser.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import live.andiirham.githubuser.R
import live.andiirham.githubuser.databinding.ActivityMainBinding
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil
import live.andiirham.githubuser.model.User
import live.andiirham.githubuser.view.adapter.OnItemClickCallback
import live.andiirham.githubuser.view.adapter.UserAdapter
import live.andiirham.githubuser.view.detail.DetailActivity
import live.andiirham.githubuser.view.detail.DetailActivity.Companion.REQUEST_ADD
import live.andiirham.githubuser.view.favorites.FavoriteActivity
import live.andiirham.githubuser.view.settings.SettingsActivity
import live.andiirham.githubuser.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private val list = ArrayList<User>()
        private lateinit var mainViewModel: MainViewModel
    }

    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    private lateinit var binding: ActivityMainBinding


    override fun getDelegate() =
        baseContextWrappingDelegate ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
            baseContextWrappingDelegate = this
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = applicationContext.getString(R.string.home)

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
        searchView.queryHint = resources.getString(R.string.query_hint)
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
                // If query attempted / welcome message set HIDDEN
                binding.welcome.visibility = View.GONE
                return true
            }

            // if changed
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    // Options : Settings and favorites
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }

            R.id.favorites -> {
                startActivity(Intent(this@MainActivity, FavoriteActivity::class.java))
            }
        }
        return true
    }

    // Showing User
    private fun showList() {
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        val userAdapter =
            UserAdapter(list)
        userAdapter.notifyDataSetChanged()

        // connect the adapter
        binding.rvUsers.adapter = userAdapter

        // getting data
        mainViewModel.getUser().observe(this, Observer { userItems ->
            if (userItems != null) {
                userAdapter.setData(userItems)
                showLoading(false)
            } else {
                showLoading(false)
                val errorCode = MainViewModel.errorCode
                Toast.makeText(this, "Error $errorCode", Toast.LENGTH_LONG).show()
            }
        })

        if (list.isNotEmpty()) {
            binding.welcome.text = null
        }

        // Click Listener
        userAdapter.setOnItemClickCallback(object :
            OnItemClickCallback {
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
        startActivityForResult(detailIntent, REQUEST_ADD)
    }

    // Loading Show when query submitted
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvUsers.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    // Language Config
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, App.LANGUAGE))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageApplicationContext(context, App.LANGUAGE)
    }
}