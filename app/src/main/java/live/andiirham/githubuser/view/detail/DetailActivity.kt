package live.andiirham.githubuser.view.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import live.andiirham.githubuser.db.entity.UserFavorite
import live.andiirham.githubuser.db.helper.UserHelper
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil
import live.andiirham.githubuser.model.DetailUser
import live.andiirham.githubuser.view.adapter.SectionsPagerAdapter

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        private val TAG = DetailActivity::class.java.simpleName
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
    }

    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    private lateinit var userHelper: UserHelper
    private var favorite: UserFavorite? = null
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

        // instance db
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        favoriteStatus = intent.getBooleanExtra(EXTRA_FAVORITE, false)
        favorite = UserFavorite()

        // getting url from MainActivity
        val url = intent.getStringExtra(EXTRA_URL)
        Log.d(TAG, "showDetail: Url = $url")
        url?.let { showDetail(it) }
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

                        Log.d(
                            TAG, "onResponse: " +
                                    "Name: ${response?.name} \n " +
                                    "Username: ${response?.username} \n " +
                                    "Location: ${response?.location} \n " +
                                    "Repo: ${response?.repository} \n " +
                                    "Company: ${response?.company} \n " +
                                    "Followers: ${response?.followers} \n " +
                                    "Following: ${response?.following} \n " +
                                    "Img URL : ${response?.avatar_url}"
                        )
                        favorite?.username = response?.username
                        favorite?.avatarUrl = response?.avatar_url
                        favorite?.url = url

                    } catch (e: Exception) {
                        Log.d(TAG, "onResponse: ${e.message} : ${e.stackTrace}")
                        Toast.makeText(this@DetailActivity, "Something Error", Toast.LENGTH_LONG)
                            .show()
                        showLoading(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(TAG, "onError: ${anError?.errorCode} : ${anError?.errorDetail}")
                    Toast.makeText(
                        this@DetailActivity,
                        "Error : ${anError?.errorCode} : ${anError?.errorDetail}",
                        Toast.LENGTH_LONG
                    ).show()
                    showLoading(false)
                }

            })

        setFavoriteStatus(favoriteStatus)
        fab_fav.setOnClickListener {
            favoriteStatus = !favoriteStatus
            // database
            setFavoriteStatus(favoriteStatus)
        }

        // Tab Followers and Following
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USERNAME)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    private fun setFavoriteStatus(favoriteStatus: Boolean) {
        if (favoriteStatus) {
            // change icons
            favorite?.let { userHelper.insertFavUser(it) }
            fab_fav.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_baseline_favorite_white_24,
                    null
                )
            )
        } else {
            // change icons
            favorite?.username?.let { userHelper.deleteFavUser(it) }
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
            view_pager.visibility = View.GONE
            fab_fav.visibility = View.GONE
        } else {
            detailProgressBar.visibility = View.GONE
            view_pager.visibility = View.VISIBLE
            fab_fav.visibility = View.VISIBLE
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

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }
}
