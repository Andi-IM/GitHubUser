package live.andiirham.githubuser.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import live.andiirham.githubuser.R

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(object:Runnable {
            override fun run() {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }
        }, 2000)
    }
}