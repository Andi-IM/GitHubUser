package live.andiirham.githubuser

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    private lateinit var dataName: Array<String>
    private lateinit var dataUsername: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataCompanies: Array<String>
    private lateinit var dataLoc: Array<String>
    private lateinit var dataFollower: Array<String>
    private lateinit var dataFollow: Array<String>
    private lateinit var dataAvatar: TypedArray
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView : ListView = findViewById(R.id.user_list)
        adapter = UserAdapter(this)
        listView.divider = null
        listView.adapter = adapter
        prepare()
        addItem()
        recycle()

        listView.onItemClickListener = AdapterView.OnItemClickListener {
            //_, _, position, _ -> Toast.makeText(this@MainActivity, users[position].username, Toast.LENGTH_SHORT).show()
            _, _, position, _ ->
                val detailUser = User(
                    dataName[position],
                    dataUsername[position],
                    dataLoc[position],
                    dataRepository[position],
                    dataCompanies[position],
                    dataFollower[position],
                    dataFollow[position],
                    dataAvatar.getResourceId(position, -1)
                )
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_USER, detailUser)
                startActivity(detailIntent)
        }
    }

    private fun recycle() {
        dataAvatar.recycle()
    }

    private fun prepare() {
        dataUsername = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataRepository = resources.getStringArray(R.array.repository)
        dataCompanies = resources.getStringArray(R.array.company)
        dataLoc = resources.getStringArray(R.array.location)
        dataFollower = resources.getStringArray(R.array.followers)
        dataFollow = resources.getStringArray(R.array.following)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addItem() {
        for (position in dataUsername.indices) {
            val user = User(
                dataName[position],
                dataUsername[position],
                dataLoc[position],
                dataRepository[position],
                dataCompanies[position],
                dataFollower[position],
                dataFollow[position],
                dataAvatar.getResourceId(position, -1)
            )
            users.add(user)
        }
        adapter.users = users
    }
}