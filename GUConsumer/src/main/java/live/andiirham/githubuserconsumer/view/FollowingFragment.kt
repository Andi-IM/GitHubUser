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
import live.andiirham.githubuserconsumer.databinding.FragmentFollowingBinding
import live.andiirham.githubuserconsumer.db.entity.User
import live.andiirham.githubuserconsumer.view.adapter.FollowAdapter
import live.andiirham.githubuserconsumer.viewmodel.FollowingViewModel

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

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFollowing.setHasFixedSize(true)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)
        followingViewModel.setUser(username)
        getProgressbar(true)
        showList()
    }

    private fun showList() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        val adapter = FollowAdapter(
            this.context,
            list
        )
        adapter.notifyDataSetChanged()
        binding.rvFollowing.adapter = adapter

        // get data from ViewModel
        followingViewModel.getUser().observe(viewLifecycleOwner, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                getProgressbar(false)
            } else {
                binding.rvFollowing.visibility = View.GONE
                binding.tvNofollowing.text = getString(R.string.no_following)

                // Error Showing
                val errorCode = FollowingViewModel.errorCode
                if (!errorCode.isBlank()) {
                    Toast.makeText(
                        activity,
                        "Following Tab Error $errorCode",
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
            binding.pbFollowing.visibility = View.VISIBLE
        } else binding.pbFollowing.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}