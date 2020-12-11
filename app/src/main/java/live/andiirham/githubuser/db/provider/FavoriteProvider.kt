package live.andiirham.githubuser.db.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import live.andiirham.githubuser.db.UserContract.AUTHORITY
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.CONTENT_URI
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.TABLE_NAME
import live.andiirham.githubuser.db.helper.UserHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private const val USERNAME = 2
        private lateinit var userHelper: UserHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // content://live.andiirham.githubuser/favorites
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)

            // content://live.andiirham.githubuser/favorites/#/user
            sUriMatcher.addURI(
                AUTHORITY, "$TABLE_NAME/*/user",
                USERNAME
            )
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        Log.d("PROVIDER", "query: ${sUriMatcher.match(uri)}")
        return when (sUriMatcher.match(uri)) {
            USER -> userHelper.queryAll()
            USERNAME -> {
                // a tricky path to parsing a username ...
                // hope to feedback for more efficient solution

                val path = uri.path
                val getUsername = path!!.split("/").toTypedArray()
                val username = getUsername[2]
                userHelper.queryByUsername(username)
            }
            else -> null
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USERNAME) {
            sUriMatcher.match(uri) -> {
                // a tricky path to parsing a username ...
                // its same thing with query to get username
                // hope to feedback for more efficient solution

                val path = uri.path
                val getUsername = path!!.split("/").toTypedArray()
                val username = getUsername[2]
                userHelper.deleteByUsername(username)
            }
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}