package es.jarroyo.mvvmcoroutines.app.di.component

import dagger.Component
import es.jarroyo.mvvmcoroutines.app.di.module.*
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.detail.fragment.ContributorListFragmentComponent
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.detail.fragment.ContributorListFragmentModule
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity.HomeActivityComponent
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity.HomeActivityModule
import es.jarroyo.mvvmcoroutines.app.di.viewmodel.ViewModelFactoryModule
import es.jarroyo.mvvmcoroutines.app.di.viewmodel.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        UtilsModule::class,
        RepositoryModule::class,
        DataModule::class,
        DomainModule::class,
        ViewModelFactoryModule::class,
        ViewModelModule::class
    )
)
interface ApplicationComponent {
    /**
     * UI - ACTIVITY
     */
    fun plus(module: HomeActivityModule): HomeActivityComponent

    /**
     * DETAIL
     */
    fun plus(module: ContributorListFragmentModule): ContributorListFragmentComponent
}