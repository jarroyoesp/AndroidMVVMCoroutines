package es.jarroyo.mvvmcoroutines.app.di.module

import com.microhealth.lmc.utils.NetworkSystem
import com.microhealth.lmc.utils.NetworkSystemAbstract
import dagger.Module
import dagger.Provides
import es.jarroyo.mvvmcoroutines.ui.App
import javax.inject.Singleton

@Module
class UtilsModule {
    @Provides
    @Singleton
    fun provideNetworkSystem(app: App) =
            NetworkSystem(app) as NetworkSystemAbstract
}