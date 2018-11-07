package es.jarroyo.mvvmcoroutines.ui.detail.model

import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response

sealed class ContributorListState {
    abstract val response: Response<List<GithubAPI.Contributor>>
}
data class DefaultContributorListState(override val response: Response<List<GithubAPI.Contributor>>) : ContributorListState()
data class LoadingContributorListState(override val response: Response<List<GithubAPI.Contributor>>) : ContributorListState()
data class ErrorContributorListState(val errorMessage: String,  override val response: Response<List<GithubAPI.Contributor>>) : ContributorListState()