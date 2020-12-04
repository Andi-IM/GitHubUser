package live.andiirham.githubuser.db.helper

import android.database.Cursor
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.entity.UserFavorite

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserFavorite> {
        val userList = ArrayList<UserFavorite>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
                userList.add(UserFavorite(username, avatarUrl, url))
            }
        }
        return userList
    }
}