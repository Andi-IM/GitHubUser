package live.andiirham.githubuserconsumer.view

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import live.andiirham.githubuserconsumer.databinding.ActivityMainBinding
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.CONTENT_URI
import live.andiirham.githubuserconsumer.db.entity.User
import live.andiirham.githubuserconsumer.db.helper.MappingHelper
import live.andiirham.githubuserconsumer.view.adapter.UserAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorites"

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = UserAdapter(this)
        binding.rvUsers.adapter = adapter

        // Threading
        val handlerThread = HandlerThread("Data Observer")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadUserAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
        /**
         * Cek savedInstanceState
         */
        if (savedInstanceState == null) {
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listUsers = list
            }
        }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val defferedUsers = async(Dispatchers.IO) {
                // CONTENT_URI = content://live.andiirham.githubuser/favorites/
                val cursor = contentResolver.query(
                    CONTENT_URI, null,
                    null, null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorite = defferedUsers.await()
            showLoading(false)
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

    /**
     * Digunakan untuk memperlihatkan progresbar
     *
     * @param state sebagai "saklar"
     */
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvUsers.visibility = View.GONE
            //binding.pbFavorite.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.VISIBLE
            //binding.pbFavorite.visibility = View.GONE
        }
    }

    /**
     *  Tampilan snackbar
     *
     *  @param message as input
     */
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvUsers, message, Snackbar.LENGTH_SHORT).show()
    }
}