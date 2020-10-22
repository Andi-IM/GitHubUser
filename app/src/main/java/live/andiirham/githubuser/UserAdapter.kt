package live.andiirham.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(private val listUsers: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>) {
        listUsers.clear()
        listUsers.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(mView)
    }

    override fun getItemCount(): Int = listUsers.size
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(img_avatar)
                tv_username.text = user.name
                tv_company.text = user.company
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }

            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}



