package live.andiirham.githubuser.db.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.TABLE_NAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion._ID
import live.andiirham.githubuser.db.entity.Favorite
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
            "$_ID DESC",
            null
        )
    }

    fun getAllUser(): ArrayList<Favorite> {
        val arrayList = ArrayList<Favorite>()
        val cursor = database.query(
            DATABASE_TABLE, null, null, null,
            null, null, null, null
        )
        cursor.moveToFirst()
        var user: Favorite
        if (cursor.count > 0) {
            do {
                user = Favorite()
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

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "${BaseColumns._ID} = ?",
            arrayOf(id),
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

    fun insertFavUser(favorite: Favorite): Long {
        val args = ContentValues()
        args.put(COLUMN_NAME_USERNAME, favorite.username)
        args.put(COLUMN_NAME_AVATAR_URL, favorite.avatarUrl)
        args.put(COLUMN_NAME_URL, favorite.url)
        return database.insert(DATABASE_TABLE, null, args)
    }

    fun deleteByUsername(id: String): Int {
        return database.delete(
            TABLE_NAME,
            "$_ID = '$id'",
            null
        )
    }
}