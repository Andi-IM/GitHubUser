package live.andiirham.githubuserconsumer.db.helper

import android.database.Cursor
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuserconsumer.db.entity.User

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
                userList.add(User(username, avatarUrl, url))
            }
        }
        return userList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): User {
        var user = User()
        favoritesCursor?.apply {
            moveToFirst()
            val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
            val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
            val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
            user = User(username, avatarUrl, url)
        }
        return user
    }
}