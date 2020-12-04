package live.andiirham.githubuser.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*
import live.andiirham.githubuser.R
import live.andiirham.githubuser.db.entity.UserFavorite
import live.andiirham.githubuser.model.User
import live.andiirham.githubuser.view.detail.DetailActivity

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    var listUsers = ArrayList<UserFavorite>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)

            notifyDataSetChanged()
        }

    fun addItem(favorite: UserFavorite) {
        this.listUsers.add(favorite)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        this.listUsers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUsers.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return FavoriteHolder(view)
    }

    override fun getItemCount(): Int = this.listUsers.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bind(listUsers[position])

    }

    inner class FavoriteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: UserFavorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favorite.avatarUrl)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_avatar)
                tv_username.text = favorite.username
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.EXTRA_URL, favorite.url)
                                intent.putExtra(DetailActivity.EXTRA_USERNAME, favorite.username)
                                intent.putExtra(DetailActivity.EXTRA_FAVORITE, true)
                                activity.startActivity(intent)
                            }
                        })
                )
            }
        }
    }
}

interface OnFavoriteItemClickCallback {
    fun onItemClicked(data: User)
}

