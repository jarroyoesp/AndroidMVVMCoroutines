package es.jarroyo.mvvmcoroutines.app.di.module

import com.microhealth.lmc.utils.NetworkSystemAbstract
import dagger.Module
import dagger.Provides
import es.jarroyo.mvvmcoroutines.data.source.disk.DiskDataSource
import es.jarroyo.mvvmcoroutines.data.source.network.INetworkDataSource
import es.jarroyo.mvvmcoroutines.data.source.network.NetworkDataSource
import es.jarroyo.mvvmcoroutines.ui.App
import javax.inject.Singleton

@Module
class DataModule {

    @Provides @Singleton
    fun provideDiskDataSource(appContext: App) =
            DiskDataSource(appContext)

    @Provides @Singleton
    fun provideNetworkDataSource(networkSystemBase: NetworkSystemAbstract) =
            NetworkDataSource(networkSystemBase) as INetworkDataSource
}
