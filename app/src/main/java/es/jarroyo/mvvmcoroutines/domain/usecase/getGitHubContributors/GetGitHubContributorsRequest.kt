package es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors

import es.jarroyo.mvvmcoroutines.domain.usecase.base.BaseRequest

class GetGitHubContributorsRequest(var owner: String, var repositorie: String) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}