package live.andiirham.githubuser.view.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import live.andiirham.githubuser.databinding.ActivityFavoriteBinding
import live.andiirham.githubuser.db.entity.UserFavorite
import live.andiirham.githubuser.db.helper.MappingHelper
import live.andiirham.githubuser.db.helper.UserHelper
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil
import live.andiirham.githubuser.view.adapter.FavoriteAdapter
import live.andiirham.githubuser.view.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var userHelper: UserHelper
    private lateinit var adapter: FavoriteAdapter
    private var baseContextWrappingDelegate: AppCompatDelegate? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun getDelegate() =
        baseContextWrappingDelegate ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
            baseContextWrappingDelegate = this
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.rvFav.adapter = adapter

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        /**
         * Cek savedInstanceState
         */
        if (savedInstanceState == null) {
            getUser()
        } else {
            val list = savedInstanceState.getParcelableArrayList<UserFavorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listUsers = list
            }
        }
    }

    private fun getUser() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedUsers = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            showLoading(false)
            val favorite = defferedUsers.await()
            if (favorite.size > 0) {
                adapter.listUsers = favorite
            } else {
                adapter.listUsers = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUsers)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                // Akan dipanggil jika request codenya ADD
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val note = data.getParcelableExtra<UserFavorite>(DetailActivity.EXTRA_FAVORITE)

                    if (note != null) {
                        adapter.addItem(note)
                    }
                    binding.rvFav.smoothScrollToPosition(adapter.itemCount - 1)
                }
                // Akan dipanggil jika request code DELETE
                DetailActivity.RESULT_DELETE -> {
                    val position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                }
            }
        }
    }

    // Back key Pressing
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Digunakan untuk memperlihatkan progresbar
     *
     * @param state sebagai "saklar"
     */
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvFav.visibility = View.GONE
            binding.pbFavorite.visibility = View.VISIBLE
        } else {
            binding.rvFav.visibility = View.VISIBLE
            binding.pbFavorite.visibility = View.GONE
        }
    }

    /**
     *  Tampilan snackbar
     *
     *  @param message as input
     */
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFav, message, Snackbar.LENGTH_SHORT).show()
    }

    // Language Config
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, App.LANGUAGE))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageApplicationContext(context, App.LANGUAGE)
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }
}