package live.andiirham.githubuser.db.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.TABLE_NAME
import live.andiirham.githubuser.db.entity.UserFavorite
import java.sql.SQLException

class UserHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: UserHelper? = null

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen) database.close()
    }

    // get all data
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }

    // insert data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    // delete data
    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_NAME_USERNAME = '$username'", null)
    }

    fun getAllUser(): ArrayList<UserFavorite> {
        val arrayList = ArrayList<UserFavorite>()
        val cursor = database.query(
            DATABASE_TABLE, null, null, null, null, null,
            null, null
        )
        cursor.moveToFirst()
        var user: UserFavorite
        if (cursor.count > 0) {
            do {
                user = UserFavorite()
                user.username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_USERNAME))
                user.avatarUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_NAME_AVATAR_URL
                    )
                )
                user.url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_URL))

                arrayList.add(user)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursor.close()
        return arrayList
    }

    fun insertFavUser(favorite: UserFavorite): Long {
        val args = ContentValues()
        args.put(COLUMN_NAME_USERNAME, favorite.username)
        args.put(COLUMN_NAME_AVATAR_URL, favorite.avatarUrl)
        args.put(COLUMN_NAME_URL, favorite.url)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteFavUser(username: String): Int {
        return database.delete(TABLE_NAME, "$COLUMN_NAME_USERNAME = '$username'", null)
    }
}