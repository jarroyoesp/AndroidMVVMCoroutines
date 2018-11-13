package es.jarroyo.mvvmcoroutines.ui.home.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposRequest
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposUseCase
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.LIMIT_REPOS_LIST
import es.jarroyo.mvvmcoroutines.ui.home.model.*
import kotlinx.coroutines.*
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
        stateLiveData.value = DefaultState(0, false, Response(emptyList()))
    }

    fun updateRepositorieList() {
        uiScope.launch(exceptionHandler) {
            val pageNum = obtainCurrentPageNum()
            stateLiveData.value = if (pageNum == 0)
                LoadingState(pageNum, false, obtainCurrentData())
            else
                PaginatingState(pageNum, false, obtainCurrentData())
            getRepositorieList(pageNum)
        }
    }

    fun resetRepositorieList() {
        uiScope.launch(exceptionHandler) {
            val pageNum = 0
            stateLiveData.value = LoadingState(pageNum, false, Response(emptyList()))
            updateRepositorieList()
        }
    }

    fun restoreRepositorieList() {
        val pageNum = obtainCurrentPageNum()
        stateLiveData.value = DefaultState(pageNum, false, obtainCurrentData())
    }

    private fun getRepositorieList(page: Int) {
        uiScope.launch(exceptionHandler) {
            val request = GetGitHubReposRequest("jarroyoesp")
            val responseRepositorieList = getGitHubReposUseCase.execute(request)
            proccessResponse(responseRepositorieList)
        }
    }

    private fun proccessResponse(response: Response<List<GithubAPI.Repo>>){
        if (response.error == null && response.data != null) {
            onRepositorieListReceived(response.data!!)
        } else if (response.error != null) {
            onError(response.error!!)
        }
    }

    private fun onRepositorieListReceived(repositorieList: List<GithubAPI.Repo>) {
        val currentRepositorieList = obtainCurrentData().data!!.toMutableList()
        val currentPageNum = obtainCurrentPageNum() + 1
        val areAllItemsLoaded = repositorieList.size < LIMIT_REPOS_LIST
        currentRepositorieList.addAll(repositorieList)
        stateLiveData.value = DefaultState(currentPageNum, areAllItemsLoaded, Response(currentRepositorieList))
    }

    private fun onError(error: String) {
        val pageNum = stateLiveData.value?.pageNum ?: 0
        stateLiveData.value =
                ErrorState(error ?: "", pageNum, obtainCurrentLoadedAllItems(), obtainCurrentData())
    }

    private fun obtainCurrentPageNum() = stateLiveData.value?.pageNum ?: 0

    private fun obtainCurrentData() = stateLiveData.value?.response ?: Response(emptyList())

    private fun obtainCurrentLoadedAllItems() = stateLiveData.value?.loadedAllItems ?: false


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e("Exception", ":" + throwable)
        val pageNum = stateLiveData.value?.pageNum ?: 0
        stateLiveData.value =
                ErrorState(throwable.toString() ?: "", pageNum, obtainCurrentLoadedAllItems(), obtainCurrentData())
    }

    override fun onCleared() {
        super.onCleared()
        presenterJob.cancel()
    }
}