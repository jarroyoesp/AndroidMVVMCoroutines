package es.jarroyo.mvvmcoroutines.ui.home.model

import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response

sealed class ReposiorieListState {
    abstract val pageNum:Int
    abstract val loadedAllItems:Boolean
    abstract val response: Response<List<GithubAPI.Repo>>
}
data class DefaultState(override val pageNum: Int, override val loadedAllItems: Boolean, override val response: Response<List<GithubAPI.Repo>>) : ReposiorieListState()
data class LoadingState(override val pageNum: Int, override val loadedAllItems: Boolean, override val response: Response<List<GithubAPI.Repo>>) : ReposiorieListState()
data class PaginatingState(override val pageNum: Int, override val loadedAllItems: Boolean, override val response: Response<List<GithubAPI.Repo>>) : ReposiorieListState()
data class ErrorState(val errorMessage: String, override val pageNum: Int, override val loadedAllItems: Boolean, override val response: Response<List<GithubAPI.Repo>>) : ReposiorieListState()