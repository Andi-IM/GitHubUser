package live.andiirham.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import live.andiirham.githubuser.detail.DetailActivity

class MainActivity : AppCompatActivity() {
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_users.setHasFixedSize(true)
        list.addAll(getListUsers())
        supportActionBar?.title = resources.getString(R.string.home)

        showList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Cari User"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // if submitted
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()
                getUserOnline(query)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return true
    }

    // ini tetap ada untuk sebagai pemanis tampilan :)
    private fun getListUsers(): Collection<User> {
        val dataUsername = resources.getStringArray(R.array.username)
        val dataName = resources.getStringArray(R.array.name)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataCompanies = resources.getStringArray(R.array.company)
        val dataLoc = resources.getStringArray(R.array.location)
        val dataFollower = resources.getStringArray(R.array.followers)
        val dataFollow = resources.getStringArray(R.array.following)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)

        val listUsers = ArrayList<User>()
        for (position in dataName.indices) {
            val user = User(
                dataName[position],
                dataUsername[position],
                dataLoc[position],
                dataRepository[position],
                dataCompanies[position],
                dataFollower[position],
                dataFollow[position],
                dataAvatar.getResourceId(position, -1)
            )
            listUsers.add(user)
        }
        return listUsers
    }

    // ini apablila kueri dijalankan
    private fun getUserOnline(kueri: String?): Collection<User> {
        return getListUsers()
    }

    private fun showList() {
        rv_users.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(list)
        rv_users.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_USER, user)
        startActivity(detailIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}