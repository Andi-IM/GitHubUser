package live.andiirham.githubuser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import live.andiirham.githubuser.api.GhUsersService
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideGhUsersService(): GhUsersService {
        return GhUsersService.create()
    }
}