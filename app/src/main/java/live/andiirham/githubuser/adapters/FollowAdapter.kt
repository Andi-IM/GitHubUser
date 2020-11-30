package live.andiirham.githubuser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import live.andiirham.githubuser.databinding.ItemUserBinding
import live.andiirham.githubuser.model.User

class FollowAdapter(private val mData: ArrayList<User>) :
    RecyclerView.Adapter<FollowAdapter.FollowersViewHolder>() {

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val itemBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowersViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = mData.size
    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) =
        holder.bind(mData[position])

    inner class FollowersViewHolder(private val itemBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(RequestOptions().override(55, 55))
                .into(itemBinding.imgAvatar)
            itemBinding.tvUsername.text = user.username
        }
    }
}
