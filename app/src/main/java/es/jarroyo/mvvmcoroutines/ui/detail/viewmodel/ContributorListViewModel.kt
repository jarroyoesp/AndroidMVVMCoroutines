package es.jarroyo.mvvmcoroutines.ui.detail.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors.GetGitHubContributorsRequest
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors.GetGitHubContributorsUseCase
import es.jarroyo.mvvmcoroutines.ui.detail.model.ContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.DefaultContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.ErrorContributorListState
import es.jarroyo.mvvmcoroutines.ui.detail.model.LoadingContributorListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributorListViewModel
    @Inject
    constructor(private val getGitHubContributorsUseCase: GetGitHubContributorsUseCase) : ViewModel() {
    /**
     * This is the job for all coroutines started by this Presenter.
     * Cancelling this job will cancel all coroutines started by this Presenter.
     */
    private val presenterJob = Job()

    /**
     * This is the main scope for all coroutines launched by Presenter.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * presenterJob.cancel()
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + presenterJob)


    val stateLiveData = MutableLiveData<ContributorListState>()

    init {
        stateLiveData.value = DefaultContributorListState(Response(emptyList()))
    }

    fun updateContributorList(respositorieName: String) {
        uiScope.launch {
            stateLiveData.value =
                LoadingContributorListState(obtainCurrentData())
            getContributorList(respositorieName)
        }
    }

    fun resetContributorList(respositorieName: String) {
        uiScope.launch {
            val pageNum = 0
            stateLiveData.value = LoadingContributorListState(Response(emptyList()))
            updateContributorList(respositorieName)
        }
    }

    fun restoreContributorList() {
        stateLiveData.value = DefaultContributorListState(obtainCurrentData())
    }

    private fun getContributorList(respositorieName: String) {
        uiScope.launch {
            val request = GetGitHubContributorsRequest("jarroyoesp", respositorieName)
            val responseRepositorieList = getGitHubContributorsUseCase.execute(request)
            proccessResponse(responseRepositorieList)
        }
    }

    private fun proccessResponse(response: Response<List<GithubAPI.Contributor>>){
        if (response.error == null && response.data != null) {
            onContributorListReceived(response.data!!)
        } else if (response.error != null) {
            onError(response.error!!)
        }
    }

    private fun onContributorListReceived(repositorieList: List<GithubAPI.Contributor>) {
        val currentRepositorieList = obtainCurrentData().data!!.toMutableList()
        currentRepositorieList.addAll(repositorieList)
        stateLiveData.value = DefaultContributorListState(Response(currentRepositorieList))
    }

    private fun onError(error: String) {
        stateLiveData.value =
                ErrorContributorListState(error ?: "", obtainCurrentData())
    }

    private fun obtainCurrentData() = stateLiveData.value?.response ?: Response(emptyList())
}