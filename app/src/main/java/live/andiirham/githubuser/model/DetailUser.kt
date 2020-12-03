package live.andiirham.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailUser(
    var name: String? = null,
    @SerializedName("login") var username: String,
    var location: String?,
    @SerializedName("public_repos") var repository: Int,
    var company: String?,
    var followers: Int,
    var following: Int,
    var avatar_url: String?
) : Parcelable