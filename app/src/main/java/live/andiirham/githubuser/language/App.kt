package live.andiirham.githubuser.language

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        var LANGUAGE = "en"
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocalizationUtil.applyLanguageContext(base, LANGUAGE))
    }

    override fun getApplicationContext(): Context {
        val context = super.getApplicationContext()
        return LocalizationUtil.applyLanguageApplicationContext(context, LANGUAGE)
    }
}