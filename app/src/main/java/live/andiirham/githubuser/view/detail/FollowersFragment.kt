package live.andiirham.githubuser.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_followers.*
import live.andiirham.githubuser.R
import live.andiirham.githubuser.model.User
import live.andiirham.githubuser.view.adapter.FollowAdapter
import live.andiirham.githubuser.viewmodel.FollowersViewModel

class FollowersFragment : Fragment() {
    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var username: String? = null
    private lateinit var followersViewModel: FollowersViewModel
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { this.username = it.getString(ARG_USERNAME) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_followers.setHasFixedSize(true)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)
        followersViewModel.setUser(username)
        getProgressbar(true)
        showList()
    }

    private fun showList() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        val adapter = FollowAdapter(
            this.context,
            list
        )
        adapter.notifyDataSetChanged()
        rv_followers.adapter = adapter

        // Getting Data
        followersViewModel.getUser().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                getProgressbar(false)
            } else {
                rv_followers.visibility = View.GONE
                tv_nofollowers.text = getString(R.string.no_followers)

                val errorCode = FollowersViewModel.errorCode
                if (!errorCode.isBlank()) {
                    Toast.makeText(
                        activity,
                        "Followers Tab Error $errorCode",
                        Toast.LENGTH_LONG
                    ).show()
                }

                getProgressbar(false)
            }
        })
    }

    // Show Loading
    private fun getProgressbar(state: Boolean) {
        if (state) {
            pb_followers.visibility = View.VISIBLE
        } else pb_followers.visibility = View.GONE
    }
}
