package live.andiirham.githubuser.db.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite(
    var username: String? = null,
    var avatarUrl: String? = null,
    var url: String? = null
) : Parcelable