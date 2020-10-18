package live.andiirham.githubuser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = users.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        }
        var viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.tv_username)
        private val txtCompany: TextView = view.findViewById(R.id.tv_company)
        private val userAvatar: CircleImageView = view.findViewById(R.id.img_avatar)

        internal fun bind(user: User) {
            txtName.text = user.username
            txtCompany.text = user.company
            userAvatar.setImageResource(user.avatar)
        }
    }
}