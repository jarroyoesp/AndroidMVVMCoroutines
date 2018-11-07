package es.jarroyo.mvvmcoroutines.app.di.module

import dagger.Module
import dagger.Provides
import es.jarroyo.mvvmcoroutines.data.repository.GitHubRepository
import es.jarroyo.mvvmcoroutines.data.source.disk.DiskDataSource
import es.jarroyo.mvvmcoroutines.data.source.network.INetworkDataSource
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideForecastRepository(
        networkDataSource: INetworkDataSource,
        diskDataSource: DiskDataSource
    ) = GitHubRepository(networkDataSource, diskDataSource)
}