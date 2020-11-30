package live.andiirham.githubuser.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import live.andiirham.githubuser.data.entity.FavoriteUser

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favoriteuser")
    fun getAll(): List<FavoriteUser>

    @Query("SELECT * FROM favoriteuser WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<FavoriteUser>

    @Insert
    fun insertAll(vararg favoriteUsers: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)
}