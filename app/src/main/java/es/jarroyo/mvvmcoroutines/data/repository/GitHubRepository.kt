package es.jarroyo.mvvmcoroutines.data.repository

import es.jarroyo.mvvmcoroutines.data.source.disk.DiskDataSource
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.data.source.network.INetworkDataSource
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors.GetGitHubContributorsRequest
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposRequest

class GitHubRepository(
    private val networkDataSource: INetworkDataSource,
    private val diskDataSource: DiskDataSource
) {

    val TAG = GitHubRepository::class.java.simpleName

    /***********************************************************************************************
     * GET REPOSITORIES LIST
     **********************************************************************************************/
    suspend fun getGitHubRepos(request: GetGitHubReposRequest): List<GithubAPI.Repo> {
        val result = networkDataSource.getGitHubData(request)
        return result
    }

    /***********************************************************************************************
     * GET CONTRIBUTORS LIST
     **********************************************************************************************/
    suspend fun getGitHubContributors(request: GetGitHubContributorsRequest): List<GithubAPI.Contributor> {
        val result = networkDataSource.getGitHubContributors(request)
        return result
    }
}