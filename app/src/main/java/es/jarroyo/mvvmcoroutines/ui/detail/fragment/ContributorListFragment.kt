package es.jarroyo.mvvmcoroutines.ui.detail.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jarroyo.mvvmcoroutines.R
import es.jarroyo.mvvmcoroutines.app.di.component.ApplicationComponent
import es.jarroyo.mvvmcoroutines.app.di.subcomponent.detail.fragment.ContributorListFragmentModule
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.ui.base.BaseFragment
import es.jarroyo.mvvmcoroutines.ui.detail.model.ContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.DefaultContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.ErrorContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.LoadingContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.viewmodel.ContributorListViewModel
import kotlinx.android.synthetic.main.fragment_contributor_list.*
import javax.inject.Inject


private const val ARG_REPO_NAME = "ARG_REPO_NAME"

/**
 * A simple [Fragment] subclass.
 *
 */
class ContributorListFragment : BaseFragment() {

    override var layoutId = R.layout.fragment_contributor_list

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ContributorListViewModel

    private var isLoading = false

    // INTENT DATA
    private var mRepositorieName: String = ""


    private val stateObserver = Observer<ContributorListState> { state ->
        state?.let {
            when (state) {
                is DefaultContributorListState -> {
                    isLoading = false
                    //hideLoading()
                    showContributors(it.response.data!!)
                }
                is LoadingContributorListState -> {
                    isLoading = true
                    /*showLoading()*/
                }
                is ErrorContributorListState -> {
                    isLoading = false
                    /*hideLoading()
                    showError((it as ErrorState).errorMessage)*/
                }
            }
        }
    }

    override fun setupInjection(applicationComponent: ApplicationComponent) {
        applicationComponent.plus(ContributorListFragmentModule(this)).injectTo(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getExtras()
        return inflateView(inflater, container)
    }

    fun getExtras() {
        arguments?.let {
            mRepositorieName = it.getString(ARG_REPO_NAME)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ContributorListViewModel::class.java)
        observeViewModel()
        savedInstanceState?.let {
            viewModel.restoreContributorList()
        } ?: viewModel.updateContributorList(mRepositorieName)
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
    private fun showContributors(contributorList: List<GithubAPI.Contributor>) {
        var text = ""
        for (contributor in contributorList) {
            text = text + "${contributor.login}\n"
        }

        fragment_contributor_list_tv.text = text
    }


    companion object {
        fun newInstance(repositorieName: String) =
            ContributorListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_REPO_NAME, repositorieName)
                }
            }
    }

}
