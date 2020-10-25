package live.andiirham.githubuser.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_following.*
import live.andiirham.githubuser.R
import live.andiirham.githubuser.model.User
import live.andiirham.githubuser.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {
    companion object {
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var username: String? = null
    private val list = ArrayList<User>()
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { username = it.getString(ARG_USERNAME) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_following.setHasFixedSize(true)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)
        followingViewModel.setUser(username)
        showList()
    }


    private fun showList() {
        rv_following.layoutManager = LinearLayoutManager(activity)
        val adapter = FollowAdapter(this.context, list)
        adapter.notifyDataSetChanged()
        rv_following.adapter = adapter

        followingViewModel.getUser().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
            } else {
                tv_nofollowing.text = getString(R.string.no_followers)
                rv_following.visibility = View.GONE
            }
        })
    }
}