package live.andiirham.githubuser.data

import androidx.room.Database
import androidx.room.RoomDatabase
import live.andiirham.githubuser.data.dao.FavoriteDao
import live.andiirham.githubuser.data.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class DatabaseFavorite : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}