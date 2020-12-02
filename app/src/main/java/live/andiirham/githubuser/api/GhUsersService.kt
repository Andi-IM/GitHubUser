package live.andiirham.githubuser.api

import live.andiirham.githubuser.BuildConfig
import live.andiirham.githubuser.data.GhSearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GhUsersService {

    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") username: String
    ): GhSearchResponse

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GhUsersService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GhUsersService::class.java)
        }
    }
}