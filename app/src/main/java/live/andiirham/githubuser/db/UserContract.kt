package live.andiirham.githubuser.db

import android.provider.BaseColumns

class UserContract {
    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            val COLUMN_NAME_USERNAME = "username"
            val COLUMN_NAME_AVATAR_URL = "avatar_url"
            val COLUMN_NAME_URL = "url"
        }
    }
}