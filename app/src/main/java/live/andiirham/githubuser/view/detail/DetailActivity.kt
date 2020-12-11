package live.andiirham.githubuser.view.detail

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import androidx.core.content.res.ResourcesCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import live.andiirham.githubuser.BuildConfig.GITHUB_API
import live.andiirham.githubuser.R
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.CONTENT_URI
import live.andiirham.githubuser.db.entity.Favorite
import live.andiirham.githubuser.db.helper.MappingHelper
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil
import live.andiirham.githubuser.model.DetailUser
import live.andiirham.githubuser.view.adapter.SectionsPagerAdapter

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_STATE = "extra_state"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
    }

    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    private var isDBExist = false
    private var favorite: Favorite? = null
    private var position: Int = 0
    private lateinit var uriWithUname: Uri
    private var favoriteStatus: Boolean = false

    override fun getDelegate() =
        baseContextWrappingDelegate ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
            baseContextWrappingDelegate = this
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteStatus = intent.getBooleanExtra(EXTRA_STATE, false)
        favorite = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (favorite != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isDBExist = true
        } else {
            favorite = Favorite()
        }

        if (isDBExist) {
            uriWithUname = Uri.parse("$CONTENT_URI/${favorite?.username}/user")
            val cursor = contentResolver.query(uriWithUname, null, null, null, null)
            if (cursor != null) {
                if (cursor.count > 0) {
                    favorite = MappingHelper.mapCursorToObject(cursor)
                    favoriteStatus = true
                    cursor.close()
                } else {
                    isDBExist = false
                    favoriteStatus = false
                }
            }
        }
        // getting url from MainActivity
        val url = intent.getStringExtra(EXTRA_URL)
        url?.let { showDetail(it) }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fab_fav) {

            favoriteStatus = !favoriteStatus
            // database
            val values = ContentValues()
            values.put(COLUMN_NAME_USERNAME, favorite?.username)
            values.put(COLUMN_NAME_AVATAR_URL, favorite?.avatarUrl)
            values.put(COLUMN_NAME_URL, favorite?.url)

            if (favoriteStatus && !isDBExist) {
                contentResolver.insert(CONTENT_URI, values)
            } else {
                if (isDBExist) {
                    contentResolver.delete(uriWithUname, null, null)
                }
            }
            setFavoriteStatus(favoriteStatus)
        }
    }

    private fun showDetail(url: String) {
        showLoading(true)

        // Getting API
        AndroidNetworking.get(url)
            .addHeaders("Authorization", "token $GITHUB_API")
            .build()
            .getAsObject(DetailUser::class.java, object : ParsedRequestListener<DetailUser> {
                override fun onResponse(response: DetailUser?) {

                    try {
                        // Draw the itemViews
                        Glide.with(this@DetailActivity)
                            .load(response?.avatar_url)
                            .apply(RequestOptions().override(55, 55))
                            .into(img_avatar_detail)

                        // If name = null or Empty
                        if (response?.name.isNullOrEmpty()) tv_name.text = response?.username
                        else tv_name.text = response?.name

                        tv_userId.text = response?.username

                        if (response?.location.isNullOrEmpty()) tv_location.visibility = View.GONE
                        else tv_location.text = response?.location

                        tv_repo.text =
                            getString(R.string.repositories, response?.repository.toString())

                        if (!response?.company.isNullOrEmpty()) tv_detail_company.text =
                            response?.company
                        else tv_detail_company.visibility = View.GONE

                        tv_followers.text =
                            getString(R.string.followers, response?.followers.toString())

                        tv_following.text =
                            getString(R.string.following, response?.following.toString())

                        showLoading(false)

                        favorite?.username = response?.username
                        favorite?.avatarUrl = response?.avatar_url
                        favorite?.url = url

                    } catch (e: Exception) {
                        toast("Something Error")
                        showLoading(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    toast("Error : ${anError?.errorCode} : ${anError?.errorDetail}")
                    showLoading(false)
                }
            })

        setFavoriteStatus(favoriteStatus)
        fab_fav.setOnClickListener(this)

        // Tab Followers and Following
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = favorite?.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setFavoriteStatus(favoriteStatus: Boolean) {
        if (favoriteStatus) {
            // change icons
            fab_fav.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_white_24,
                    null
                )
            )
        } else {
            // change icons
            fab_fav.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_border_white_24,
                    null
                )
            )
        }
    }

    // Back key Pressing
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Loading
    private fun showLoading(state: Boolean) {
        if (state) {
            detailProgressBar.visibility = View.VISIBLE
            fab_fav.hide()
        } else {
            detailProgressBar.visibility = View.GONE
            fab_fav.show()
        }
    }

    // Lang Config
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, App.LANGUAGE))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageApplicationContext(context, App.LANGUAGE)
    }
}
