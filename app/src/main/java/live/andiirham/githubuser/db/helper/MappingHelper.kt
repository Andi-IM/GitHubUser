package live.andiirham.githubuser.db.helper

import android.database.Cursor
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion._ID
import live.andiirham.githubuser.db.entity.Favorite

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<Favorite> {
        val userList = ArrayList<Favorite>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
                userList.add(Favorite(id, username, avatarUrl, url))
            }
        }
        return userList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): Favorite {
        var favorite = Favorite()
        favoritesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
            val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
            val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
            favorite = Favorite(id, username, avatarUrl, url)
        }
        return favorite
    }
}