package live.andiirham.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

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