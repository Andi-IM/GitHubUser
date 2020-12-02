package live.andiirham.githubuser.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import live.andiirham.githubuser.storage.SharedPreferencesStorage
import live.andiirham.githubuser.storage.Storage

@InstallIn(ApplicationComponent::class)
@Module
abstract class StorageModule {
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}