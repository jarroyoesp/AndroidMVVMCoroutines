package es.jarroyo.mvvmcoroutines.ui.home.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposRequest
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposUseCase
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.LIMIT_REPOS_LIST
import es.jarroyo.mvvmcoroutines.ui.home.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class RepositorieListViewModel
    @Inject
    constructor(private val getGitHubReposUseCase: GetGitHubReposUseCase) : ViewModel() {
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


    val stateLiveData = MutableLiveData<ReposiorieListState>()

    init {
        stateLiveData.value = DefaultState(0, false, emptyList())
    }

    fun updateRepositorieList() {
        uiScope.launch {
            val pageNum = obtainCurrentPageNum()
            stateLiveData.value = if (pageNum == 0)
                LoadingState(pageNum, false, obtainCurrentData())
            else
                PaginatingState(pageNum, false, obtainCurrentData())
            getRepositorieList(pageNum)
        }
    }

    fun resetRepositorieList() {
        uiScope.launch {
            val pageNum = 0
            stateLiveData.value = LoadingState(pageNum, false, emptyList())
            updateRepositorieList()
        }
    }

    fun restoreRepositorieList() {
        val pageNum = obtainCurrentPageNum()
        stateLiveData.value = DefaultState(pageNum, false, obtainCurrentData())
    }

    private fun getRepositorieList(page: Int) {
        uiScope.launch {
            val request = GetGitHubReposRequest("jarroyoesp")
            val responseRepositorieList = getGitHubReposUseCase.execute(request)
            onRepositorieListReceived(responseRepositorieList.data!!)
        }
    }

    private fun onRepositorieListReceived(repositorieList: List<GithubAPI.Repo>) {
        val currentRepositorieList = obtainCurrentData().toMutableList()
        val currentPageNum = obtainCurrentPageNum() + 1
        val areAllItemsLoaded = repositorieList.size < LIMIT_REPOS_LIST
        currentRepositorieList.addAll(repositorieList)
        stateLiveData.value = DefaultState(currentPageNum, areAllItemsLoaded, currentRepositorieList)
    }

    private fun onError(error: Throwable) {
        val pageNum = stateLiveData.value?.pageNum ?: 0
        stateLiveData.value =
                ErrorState(error.message ?: "", pageNum, obtainCurrentLoadedAllItems(), obtainCurrentData())
    }

    private fun obtainCurrentPageNum() = stateLiveData.value?.pageNum ?: 0

    private fun obtainCurrentData() = stateLiveData.value?.data ?: emptyList()

    private fun obtainCurrentLoadedAllItems() = stateLiveData.value?.loadedAllItems ?: false

}