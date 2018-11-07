package es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity

import dagger.Module
import es.jarroyo.mvvmcoroutines.app.di.module.ActivityModule
import es.jarroyo.mvvmcoroutines.ui.home.activity.HomeActivity

@Module
class HomeActivityModule(activity: HomeActivity) : ActivityModule(activity) {

}