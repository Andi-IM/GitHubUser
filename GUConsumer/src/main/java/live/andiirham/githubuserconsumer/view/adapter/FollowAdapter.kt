package live.andiirham.githubuserconsumer.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import live.andiirham.githubuserconsumer.R
import live.andiirham.githubuserconsumer.databinding.ItemUserBinding
import live.andiirham.githubuserconsumer.db.entity.User

class FollowAdapter(private val context: Context?, private val mData: ArrayList<User>) :
    RecyclerView.Adapter<FollowAdapter.FollowersViewHolder>() {

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowersViewHolder {
        return FollowersViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int = mData.size
    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) =
        holder.bind(mData[position])

    inner class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgAvatar)
            binding.tvUsername.text = user.username
        }
    }
}
