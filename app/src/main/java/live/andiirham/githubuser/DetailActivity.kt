package live.andiirham.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val username : TextView = findViewById(R.id.user_name)
        val userid : TextView = findViewById(R.id.user_id)
        val avatar : ImageView = findViewById(R.id.img_avatar)
        val repo : TextView = findViewById(R.id.tv_repo)
        val company : TextView = findViewById(R.id.tv_company)
        val location : TextView = findViewById(R.id.tv_location)
        val follow : TextView = findViewById(R.id.tv_follow)
        val following : TextView = findViewById(R.id.tv_following)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val tx_repo = getString(R.string.repositories, "${user.repository}")
        val tx_follow = getString(R.string.followers, "${user.follower}")
        val tx_foll = getString(R.string.following, "${user.follow}")

        username.text = user.name
        userid.text = user.username
        avatar.setImageResource(user.avatar)
        repo.text = tx_repo
        company.text = user.company
        location.text = user.location
        follow.text = tx_follow
        following.text = tx_foll
    }
}