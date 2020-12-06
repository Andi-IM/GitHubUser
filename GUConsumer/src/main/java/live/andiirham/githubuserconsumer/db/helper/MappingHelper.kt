package live.andiirham.githubuserconsumer.db.helper

import android.database.Cursor
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion._ID
import live.andiirham.githubuserconsumer.db.entity.User

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
                val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
                userList.add(User(id, username, avatarUrl, url))
            }
        }
        return userList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): User {
        var User = User()
        favoritesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(_ID))
            val username = getString(getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
            val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_NAME_AVATAR_URL))
            val url = getString(getColumnIndexOrThrow(COLUMN_NAME_URL))
            User = User(id, username, avatarUrl, url)
        }
        return User
    }
}