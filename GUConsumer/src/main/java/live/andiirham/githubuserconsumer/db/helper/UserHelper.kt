package live.andiirham.githubuserconsumer.db.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuserconsumer.db.UserContract.UserColumns.Companion.TABLE_NAME
import live.andiirham.githubuserconsumer.db.entity.User
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
            "$COLUMN_NAME_USERNAME DESC",
            null
        )
    }

    fun getAllUser(): ArrayList<User> {
        val arrayList = ArrayList<User>()
        val cursor = database.query(
            DATABASE_TABLE, null, null, null,
            null, null, null, null
        )
        cursor.moveToFirst()
        var user: User
        if (cursor.count > 0) {
            do {
                user = User()
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

    fun queryById(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${COLUMN_NAME_USERNAME} = ?",
            arrayOf(username),
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

    fun insertFavUser(User: User): Long {
        val args = ContentValues()
        args.put(COLUMN_NAME_USERNAME, User.username)
        args.put(COLUMN_NAME_AVATAR_URL, User.avatarUrl)
        args.put(COLUMN_NAME_URL, User.url)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(
            TABLE_NAME,
            "${COLUMN_NAME_USERNAME}E = '$username'",
            null
        )
    }
}