package es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList

import es.jarroyo.mvvmcoroutines.data.exception.NetworkConnectionException
import es.jarroyo.mvvmcoroutines.data.repository.GitHubRepository
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.BaseUseCase
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response
import retrofit2.HttpException

const val LIMIT_CRYPTO_LIST = 20

class GetGitHubReposUseCase(val repository: GitHubRepository) : BaseUseCase<GetGitHubReposRequest, List<GithubAPI.Repo>>() {

    override suspend fun run(): Response<List<GithubAPI.Repo>> {
        try {
            return Response(repository.getGitHubRepos(request!!))
        } catch (e: NetworkConnectionException) {
            return Response(error = "NetworkConnectionException", exception = e)
        } catch (e: HttpException) {
            return Response(error = "HttpException", exception = e)
        }
    }
}