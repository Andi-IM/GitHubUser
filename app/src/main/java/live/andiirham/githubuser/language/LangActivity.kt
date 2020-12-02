package live.andiirham.githubuser.language

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.BaseContextWrappingDelegate
import live.andiirham.githubuser.MainActivity
import live.andiirham.githubuser.R
import live.andiirham.githubuser.databinding.ActivityLangBinding

class LangActivity : AppCompatActivity() {

    private val languages = arrayListOf("English", "Bahasa Indonesia", "日本語")

    private var baseContextWrappingDelegate: AppCompatDelegate? = null
    private lateinit var binding: ActivityLangBinding
    override fun getDelegate() =
        baseContextWrappingDelegate ?: BaseContextWrappingDelegate(super.getDelegate()).apply {
            baseContextWrappingDelegate = this
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = resources.getString(R.string.language_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listView: ListView = binding.lvLang  // findViewById(R.id.lv_lang)
        val adapter = ArrayAdapter<String>(
            this,
            // using from stock libraries
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            languages
        )
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            attachNewLanguage(position)
            Toast.makeText(this@LangActivity, languages[position], Toast.LENGTH_SHORT).show()
        }
    }

    private fun attachNewLanguage(position: Int) {
        when (position) {
            0 -> reloadActivity("en")
            1 -> reloadActivity("id")
            2 -> reloadActivity("ja")
        }
    }

    private fun reloadActivity(lang: String) {
        App.LANGUAGE = lang
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(newBase, App.LANGUAGE))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageApplicationContext(context, App.LANGUAGE)
    }

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        startActivity(intent)
        onBackPressed()
        return true
    }
}