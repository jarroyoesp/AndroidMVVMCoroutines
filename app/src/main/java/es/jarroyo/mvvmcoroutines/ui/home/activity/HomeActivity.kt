package es.jarroyo.mvvmcoroutines.ui.home.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import es.jarroyo.mvvmcoroutines.R
import es.jarroyo.mvvmcoroutines.app.di.component.ApplicationComponent
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity.HomeActivityModule
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.ui.base.BaseActivity
import es.jarroyo.mvvmcoroutines.ui.home.model.*
import es.jarroyo.mvvmcoroutines.ui.home.viewmodel.RepositorieListViewModel
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : BaseActivity() {
    override var layoutId = R.layout.activity_home

    override fun setupInjection(applicationComponent: ApplicationComponent) {
        applicationComponent.plus(HomeActivityModule(this)).injectTo(this)
    }


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: RepositorieListViewModel

    private var isLoading = false
    private var isLastPage = false

    private val stateObserver = Observer<ReposiorieListState> { state ->
        state?.let {
            isLastPage = state.loadedAllItems
            when (state) {
                is DefaultState -> {
                    isLoading = false
                    updateView(it.data)
                }
                is LoadingState -> {
                    isLoading = true
                }
                is PaginatingState -> {
                    isLoading = true
                }
                is ErrorState -> {
                    isLoading = false
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositorieListViewModel::class.java)
        observeViewModel()
        savedInstanceState?.let {
            viewModel.restoreRepositorieList()
        } ?: viewModel.updateRepositorieList()

        configView()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stateLiveData.removeObserver(stateObserver)
    }

    private fun observeViewModel() {
        viewModel.stateLiveData.observe(this, stateObserver)
    }

    /**
     * CONFIG VIEW
     */
    private fun configView() {
        activity_home_button_get_data.setOnClickListener{
            viewModel.updateRepositorieList()
        }
    }

    private fun updateView(respositoriesList: List<GithubAPI.Repo>){
        var text = ""
        for (repositorie in respositoriesList) {
            text = text + "${repositorie.name}\n"
        }
        activity_home_tv_data.text = text
    }
}
