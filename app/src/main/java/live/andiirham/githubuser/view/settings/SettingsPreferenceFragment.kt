package live.andiirham.githubuser.view.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import live.andiirham.githubuser.R

class SettingsPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}