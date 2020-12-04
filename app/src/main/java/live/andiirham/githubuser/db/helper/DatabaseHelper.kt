package live.andiirham.githubuser.db.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_AVATAR_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_URL
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.COLUMN_NAME_USERNAME
import live.andiirham.githubuser.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "githubuserdb"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE $TABLE_NAME" +
                "(${COLUMN_NAME_USERNAME} TEXT NOT NULL," +
                "${COLUMN_NAME_AVATAR_URL} TEXT NOT NULL," +
                "${COLUMN_NAME_URL} TEXT NOT NULL)"
    }

    // digunakan saat membangun db
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE)
    }

    // digunakan untuk upgrade versi
    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
        */
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}