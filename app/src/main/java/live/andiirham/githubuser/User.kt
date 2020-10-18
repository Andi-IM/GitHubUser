package live.andiirham.githubuser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var name: String?,
    var username: String?,
    var location: String?,
    var repository : String?,
    var company: String?,
    var follower: String?,
    var follow: String?,
    var avatar: Int
) : Parcelable