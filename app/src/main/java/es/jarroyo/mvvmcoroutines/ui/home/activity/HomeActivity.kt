package es.jarroyo.mvvmcoroutines.ui.home.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import es.jarroyo.mvvmcoroutines.R
import es.jarroyo.mvvmcoroutines.app.di.component.ApplicationComponent
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.home.activity.HomeActivityModule
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.ui.base.BaseActivity
import es.jarroyo.mvvmcoroutines.ui.home.activity.adapter.HomeRvAdapter
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

    // RV Adapter
    private var mLayoutManager: LinearLayoutManager? = null
    private var mRvAdapter: HomeRvAdapter? = null

    private val stateObserver = Observer<ReposiorieListState> { state ->
        state?.let {
            isLastPage = state.loadedAllItems
            when (state) {
                is DefaultState -> {
                    isLoading = false
                    hideLoading()
                    showRepositories(it.response.data!!)
                }
                is LoadingState -> {
                    isLoading = true
                    showLoading()
                }
                is PaginatingState -> {
                    isLoading = true
                }
                is ErrorState -> {
                    isLoading = false
                    hideLoading()
                    showError((it as ErrorState).errorMessage)
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

    fun configRecyclerView(repositorieList: List<GithubAPI.Repo>) {
        mLayoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL, false)
        activity_home_rv.layoutManager = mLayoutManager

        mRvAdapter = HomeRvAdapter(repositorieList, listenerRepositorieClicked = {
            // ON CLICK
            Toast.makeText(this, "OnCick: ${it.repositorue.name}", Toast.LENGTH_SHORT).show()
            navigator.addContributorListFragment(R.id.activity_home_layout_main, it.repositorue.name)
        })

        activity_home_rv.adapter = mRvAdapter

        // Add separator to recycler viewUserProfile
        /*val verticalDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL)
        val verticalDivider = ContextCompat.getDrawable(this, R.drawable.separator_vertical_rv_medications_treatment)
        verticalDecoration.setDrawable(verticalDivider!!)
        activity_home_rv.addItemDecoration(verticalDecoration)*/
    }


    private fun showRepositories(respositoriesList: List<GithubAPI.Repo>){
        configRecyclerView(respositoriesList)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(){
        activity_home_loading.visibility = View.VISIBLE
        activity_home_rv.visibility = View.GONE
    }

    private fun hideLoading(){
        activity_home_loading.visibility = View.GONE
        activity_home_rv.visibility = View.VISIBLE
    }
}
