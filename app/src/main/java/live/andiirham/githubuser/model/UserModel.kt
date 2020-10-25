package live.andiirham.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("login") var username: String?,
    @SerializedName("avatar_url") var avatar_url: String? = null,
    @SerializedName("url") var url: String? = null
) : Parcelable
