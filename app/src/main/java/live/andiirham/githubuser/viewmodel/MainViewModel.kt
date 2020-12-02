package live.andiirham.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import live.andiirham.githubuser.BuildConfig
import live.andiirham.githubuser.model.User
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class MainViewModel : ViewModel() {
    // TODO: 30/11/2020 Change api fetch to dagger 
    val listUsers = MutableLiveData<ArrayList<User>>()

    private val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
        var errorCode: String? = null
        private const val TOKEN = BuildConfig.GITHUB_TOKEN
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun setUser(user: String) {
        // Request API
        val listItems = ArrayList<User>()

        AndroidNetworking.get("https://api.github.com/search/users")
            .addQueryParameter("q", user)
            .addHeaders("Authorization", "token $TOKEN")
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val items = response?.getJSONArray("items")

                        for (i in 0 until items!!.length()) {
                            val users = items.getJSONObject(i)
                            val userItem = User(
                                users.getString("login"),
                                users.getString("avatar_url"),
                                users.getString("url")
                            )
                            Log.d(
                                TAG,
                                "onResponse: Username List = ${userItem.username.toString()}"
                            )
                            listItems.add(userItem)
                        }
                        Log.d("onResponse: ", "${listItems.size} items added")
                    } catch (e: Exception) {
                        Log.d("onResponse: ", e.message.toString())
                    }
                    //adapter.setData(itemList)
                    listUsers.postValue(listItems)
                }

                override fun onError(anError: ANError?) {
                    Log.d("onError: ", "${anError?.errorCode.toString()} : ${anError?.errorDetail}")
                    errorCode = "${anError?.errorCode.toString()} : ${anError?.errorDetail}"
                    listUsers.postValue(null)
                }
            })
    }

}