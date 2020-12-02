package live.andiirham.githubuser.data

import com.google.gson.annotations.SerializedName
import live.andiirham.githubuser.model.User

data class GhSearchResponse(
    @field:SerializedName("items") val items: List<User>,
    @field:SerializedName("total_count") val totalCount: Int
)