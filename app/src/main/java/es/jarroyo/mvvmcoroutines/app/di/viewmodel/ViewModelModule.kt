package es.jarroyo.mvvmcoroutines.app.di.viewmodel

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import es.jarroyo.mvvmcoroutines.ui.detail.viewmodel.ContributorListViewModel
import es.jarroyo.mvvmcoroutines.ui.home.viewmodel.RepositorieListViewModel
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RepositorieListViewModel::class)
    abstract fun bindRepositoriesListViewModel(viewModel: RepositorieListViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ContributorListViewModel::class)
    abstract fun bindContributorListViewModel(viewModel: ContributorListViewModel): ViewModel
}