package live.andiirham.githubuser.data

import androidx.annotation.WorkerThread
import live.andiirham.githubuser.data.dao.FavoriteDao
import live.andiirham.githubuser.data.entity.FavoriteUser

class FavRepository(private val dao: FavoriteDao) {
    // val allUsers: Flow<List<FavoriteUser>> = dao.getAll()

    @Suppress("RendudantSuspendModifier")
    @WorkerThread
    suspend fun insert(favoriteUser: FavoriteUser) {
        dao.insertAll(favoriteUser)
    }
}