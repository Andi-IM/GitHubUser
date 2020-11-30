package live.andiirham.githubuser

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import live.andiirham.githubuser.adapters.SectionsPagerAdapter
import live.andiirham.githubuser.databinding.ActivityDetailBinding
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil
import live.andiirham.githubuser.model.DetailUser

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_USERNAME = "extra_username"
        private val TAG = DetailActivity::class.java.simpleName
    }

    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    private lateinit var binding: ActivityDetailBinding

    override fun getDelegate() =
        baseContextWrappingDelegate ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
            baseContextWrappingDelegate = this
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = resources.getString(R.string.detail_user)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // getting url from MainActivity
        val url = intent.getStringExtra(EXTRA_URL)
        Log.d(TAG, "showDetail: Url = $url")
        url?.let { showDetail(it) }
    }

    private fun showDetail(url: String) {
        showLoading(true)

        // Getting API
        AndroidNetworking.get(url)
            .addHeaders("Authorization", "token 64224e2a71fbbd7965657eab4c2c4e04315bce1e")
            .build()
            .getAsObject(DetailUser::class.java, object : ParsedRequestListener<DetailUser> {
                override fun onResponse(response: DetailUser?) {

                    try {
                        // Draw the itemViews
                        Glide.with(this@DetailActivity)
                            .load(response?.avatar_url)
                            .apply(RequestOptions().override(55, 55))
                            .into(binding.imgAvatarDetail)

                        // If name = null or Empty
                        if (response?.name.isNullOrEmpty()) binding.tvName.text = response?.username
                        else binding.tvName.text = response?.name

                        binding.tvUserId.text = response?.username

                        if (response?.location.isNullOrEmpty()) binding.tvLocation.visibility =
                            View.GONE
                        else binding.tvLocation.text = response?.location

                        binding.tvRepo.text =
                            getString(R.string.repositories, response?.repository.toString())

                        if (!response?.company.isNullOrEmpty())
                            binding.tvDetailCompany.text = response?.company
                        else binding.tvDetailCompany.visibility = View.GONE

                        binding.tvFollowers.text =
                            getString(R.string.followers, response?.followers.toString())

                        binding.tvFollowing.text =
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
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        sectionsPagerAdapter.username = intent.getStringExtra(EXTRA_USERNAME)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }

    // Back key Pressing
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Loading
    private fun showLoading(state: Boolean) {
        if (state) {
            binding.detailProgressBar.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
        } else {
            binding.detailProgressBar.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
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
