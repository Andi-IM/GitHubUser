package live.andiirham.githubuserconsumer.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import live.andiirham.githubuserconsumer.R
import live.andiirham.githubuserconsumer.databinding.ItemUserBinding
import live.andiirham.githubuserconsumer.db.entity.User
import live.andiirham.githubuserconsumer.view.DetailActivity
import live.andiirham.githubuserconsumer.view.DetailActivity.Companion.EXTRA_FAVORITE
import live.andiirham.githubuserconsumer.view.DetailActivity.Companion.EXTRA_STATE
import live.andiirham.githubuserconsumer.view.DetailActivity.Companion.EXTRA_URL
import live.andiirham.githubuserconsumer.view.DetailActivity.Companion.EXTRA_USERNAME
import live.andiirham.githubuserconsumer.view.DetailActivity.Companion.REQUEST_ADD

class UserAdapter(private val activity: Activity) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    var listUsers = ArrayList<User>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)
            notifyDataSetChanged()
        }

    fun addItem(user: User) {
        this.listUsers.add(user)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        this.listUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUsers.size)
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgAvatar)
            binding.tvUsername.text = user.username
            binding.itemUser.setOnClickListener(
                CustomOnItemClickListener(
                    bindingAdapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(EXTRA_URL, user.url)
                            intent.putExtra(EXTRA_USERNAME, user.username)
                            intent.putExtra(EXTRA_FAVORITE, user)
                            intent.putExtra(EXTRA_STATE, true)
                            activity.startActivityForResult(intent, REQUEST_ADD)
                        }

                    })
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listUsers.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }
}



