package es.jarroyo.mvvmcoroutines.app.di.subcomponent.detail.fragment

import dagger.Subcomponent
import es.jarroyo.mvvmcoroutines.ui.detail.fragment.ContributorListFragment

@Subcomponent(modules = arrayOf(ContributorListFragmentModule::class))
interface ContributorListFragmentComponent {
    fun injectTo(fragment: ContributorListFragment)
}