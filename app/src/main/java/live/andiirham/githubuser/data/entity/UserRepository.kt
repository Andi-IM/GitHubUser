package live.andiirham.githubuser.data.entity

import kotlinx.coroutines.flow.Flow
import live.andiirham.githubuser.api.GhUsersService
import live.andiirham.githubuser.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: GhUsersService) {
    fun getSearchResultStream(query: String): Flow<PagingData<User>>
}