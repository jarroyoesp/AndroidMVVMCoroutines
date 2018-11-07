package es.jarroyo.mvvmcoroutines.data.source.network

import com.microhealth.lmc.utils.NetworkSystemAbstract
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposRequest

open abstract class INetworkDataSource(private val networkSystem: NetworkSystemAbstract) {

    /**
     * GET GITHUB DATA
     */
    abstract suspend fun getGitHubData(request: GetGitHubReposRequest): List<GithubAPI.Repo>

    /**
     * GET GITHUB DATA
     */
    //abstract suspend fun getGitHubContributors(request: GetGitHubContributorsRequest): List<GithubAPI.Contributor>
}