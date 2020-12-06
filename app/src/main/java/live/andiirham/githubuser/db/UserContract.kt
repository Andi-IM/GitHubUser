package live.andiirham.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    // Authority yang digunakan
    const val AUTHORITY = "live.andiirham.githubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorites"
            val _ID = "_id"
            val COLUMN_NAME_USERNAME = "username"
            val COLUMN_NAME_AVATAR_URL = "avatar_url"
            val COLUMN_NAME_URL = "url"

            // Base content yang digunakan untuk mengakses content provider
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}