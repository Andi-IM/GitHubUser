package live.andiirham.githubuserconsumer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import live.andiirham.githubuserconsumer.R
import live.andiirham.githubuserconsumer.databinding.FragmentFollowersBinding
import live.andiirham.githubuserconsumer.db.entity.User
import live.andiirham.githubuserconsumer.view.adapter.FollowAdapter
import live.andiirham.githubuserconsumer.viewmodel.FollowersViewModel

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

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollowers.setHasFixedSize(true)
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)
        followersViewModel.setUser(username)
        getProgressbar(true)
        showList()
    }

    private fun showList() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        val adapter = FollowAdapter(
            this.context,
            list
        )
        adapter.notifyDataSetChanged()
        binding.rvFollowers.adapter = adapter

        // Getting Data
        followersViewModel.getUser().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                getProgressbar(false)
            } else {
                binding.rvFollowers.visibility = View.GONE
                binding.tvNofollowers.text = getString(R.string.no_followers)

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
            binding.pbFollowers.visibility = View.VISIBLE
        } else binding.pbFollowers.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
