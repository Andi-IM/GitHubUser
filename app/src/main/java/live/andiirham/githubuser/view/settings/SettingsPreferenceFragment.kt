package live.andiirham.githubuser.view.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import live.andiirham.githubuser.R
import live.andiirham.githubuser.language.App
import live.andiirham.githubuser.view.settings.alarm.AlarmReceiver

class SettingsPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var NOTIFICATION: String
    private lateinit var LANGUAGES: String

    private lateinit var REMINDER: SwitchPreference
    private lateinit var LANGSETTINGS: Preference
    private val langList = mapOf("en" to "English", "id" to "Bahasa Indonesia", "ja" to "日本語")
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummary()
    }


    private fun init() {
        NOTIFICATION = resources.getString(R.string.notif_key)
        REMINDER = findPreference<SwitchPreference>(NOTIFICATION) as SwitchPreference
        alarmReceiver = AlarmReceiver()

        LANGUAGES = resources.getString(R.string.lang_key)
        LANGSETTINGS = findPreference<Preference>(LANGUAGES) as Preference
    }

    private fun setSummary() {
        REMINDER.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _,
                                                    isReminderObject ->
                val isReminderOn: Boolean = isReminderObject as Boolean
                if (isReminderOn) {
                    alarmReceiver.setDailyReminder(
                        requireContext(),
                        AlarmReceiver.DAILY_TYPE,
                        getString(R.string.daily_reminder_message)
                    )
                } else {
                    alarmReceiver.cancelAlarm(requireContext())
                }
                true
            }

        val intent = Intent(context, LangActivity::class.java)
        LANGSETTINGS.intent = intent
        LANGSETTINGS.summary = langList[App.LANGUAGE]
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            NOTIFICATION -> REMINDER.summary

            LANGUAGES -> REMINDER.summary
        }
    }


}
