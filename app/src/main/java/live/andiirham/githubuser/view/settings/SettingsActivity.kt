package live.andiirham.githubuser.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import live.andiirham.githubuser.R
import live.andiirham.githubuser.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(R.id.setting_holder, SettingsPreferenceFragment()).commit()
    }
}