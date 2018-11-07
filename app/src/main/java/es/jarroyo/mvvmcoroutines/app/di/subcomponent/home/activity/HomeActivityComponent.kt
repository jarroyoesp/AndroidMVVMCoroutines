package es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity

import dagger.Subcomponent
import es.jarroyo.mvvmcoroutines.ui.home.activity.HomeActivity

@Subcomponent(modules = arrayOf(HomeActivityModule::class))
interface HomeActivityComponent {
    fun injectTo(activity: HomeActivity)
}