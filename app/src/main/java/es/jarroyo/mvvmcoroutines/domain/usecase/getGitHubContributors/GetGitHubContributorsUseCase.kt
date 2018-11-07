package es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors


import es.jarroyo.mvvmcoroutines.data.exception.NetworkConnectionException
import es.jarroyo.mvvmcoroutines.data.repository.GitHubRepository
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import es.jarroyo.mvvmcoroutines.domain.usecase.base.BaseUseCase
import es.jarroyo.mvvmcoroutines.domain.usecase.base.Response
import retrofit2.HttpException

class GetGitHubContributorsUseCase(val repository: GitHubRepository) : BaseUseCase<GetGitHubContributorsRequest, List<GithubAPI.Contributor>>() {

    override suspend fun run(): Response<List<GithubAPI.Contributor>> {
        try {
            return Response(repository.getGitHubContributors(request!!))
        } catch (e: NetworkConnectionException) {
            return Response(error = "NetworkConnectionException", exception = e)
        } catch (e: HttpException) {
            return Response(error = "HttpException", exception = e)
        }
    }
}