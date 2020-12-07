package live.andiirham.githubuser.view.settings

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import live.andiirham.githubuser.R
import live.andiirham.githubuser.databinding.ActivitySettingsBinding
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.language.LocalizationUtil

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, SettingsPreferenceFragment()).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // lang settings
    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    override fun getDelegate(): AppCompatDelegate {
        return baseContextWrappingDelegate
            ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
                baseContextWrappingDelegate = this
            }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let {
            LocalizationUtil.applyLanguageContext(
                it,
                App.LANGUAGE
            )
        })
    }

    override fun getApplicationContext(): Context {
        return LocalizationUtil.applyLanguageApplicationContext(application, App.LANGUAGE)
    }
}