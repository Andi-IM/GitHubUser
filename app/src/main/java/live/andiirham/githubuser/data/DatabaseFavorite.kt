package live.andiirham.githubuser.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import live.andiirham.githubuser.data.dao.FavoriteDao
import live.andiirham.githubuser.data.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class DatabaseFavorite : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseFavorite? = null

        fun getDatabase(context: Context): DatabaseFavorite {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseFavorite::class.java,
                    "FavoriteDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}