package live.andiirham.githubuserconsumer.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail.*
import live.andiirham.githubuserconsumer.BuildConfig.GITHUB_API
import live.andiirham.githubuserconsumer.R
import live.andiirham.githubuserconsumer.db.entity.DetailUser
import live.andiirham.githubuserconsumer.view.adapter.SectionsPagerAdapter

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_STATE = "extra_state"
        private val TAG = DetailActivity::class.java.simpleName
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val url = intent.getStringExtra(EXTRA_URL)
        if (url != null) showDetail(url)
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

        // Tab Followers and Following
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USERNAME)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
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

}