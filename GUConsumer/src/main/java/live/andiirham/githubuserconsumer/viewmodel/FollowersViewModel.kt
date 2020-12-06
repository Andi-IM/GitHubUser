package live.andiirham.githubuserconsumer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import live.andiirham.githubuserconsumer.BuildConfig
import live.andiirham.githubuserconsumer.db.entity.User
import okhttp3.OkHttpClient
import org.json.JSONArray
import java.util.concurrent.TimeUnit

class FollowersViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
        private const val TOKEN =
            BuildConfig.GITHUB_API
        var errorCode: String = ""
    }

    fun getUser(): LiveData<ArrayList<User>> = listUsers

    fun setUser(username: String?) {
        // Request API
        val listItems = ArrayList<User>()

        AndroidNetworking.get("https://api.github.com/users/{username}/followers")
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $TOKEN")
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    try {

                        if (response?.length()!! > 0) {
                            for (i in 0 until response.length()) {
                                val users = response.getJSONObject(i)
                                val user = User(
                                    username = users.getString("login"),
                                    avatarUrl = users.getString("avatar_url"),
                                    url = users.getString("url")
                                )
                                Log.d(
                                    TAG,
                                    "onResponse: INI DAFTAR FOLLOWERSNYA! = ${users.getString("login")}"
                                )
                                listItems.add(user)
                                listUsers.postValue(listItems)
                            }
                        } else listUsers.postValue(null)

                        Log.d("onResponse: ", "${listItems.size} followers added")
                    } catch (e: Exception) {
                        Log.d("onResponse: ", e.message.toString())
                        errorCode = e.message.toString()
                        listUsers.postValue(null)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d(TAG, "onError: ${anError?.errorDetail}")
                    errorCode = "${anError?.errorCode.toString()} : ${anError?.errorDetail}"
                    listUsers.postValue(null)
                }
            })
    }
}