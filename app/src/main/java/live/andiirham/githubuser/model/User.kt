package live.andiirham.githubuser.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @field:SerializedName("login") var username: String?,
    @field:SerializedName("avatar_url") var avatar_url: String? = null,
    @field:SerializedName("url") var url: String? = null
) : Parcelable
