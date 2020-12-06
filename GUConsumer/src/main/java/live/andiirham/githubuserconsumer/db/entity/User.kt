package live.andiirham.githubuserconsumer.db.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: Int = 0,
    var username: String? = null,
    var avatarUrl: String? = null,
    var url: String? = null
) : Parcelable