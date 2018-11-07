package es.jarroyo.mvvmcoroutines.app.di.module

import dagger.Module
import dagger.Provides
import es.jarroyo.mvvmcoroutines.data.repository.GitHubRepository
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubContributors.GetGitHubContributorsUseCase
import es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList.GetGitHubReposUseCase
import javax.inject.Singleton


@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideGetGitHubReposUseCase(repository: GitHubRepository) = GetGitHubReposUseCase(repository)


    @Provides
    @Singleton
    fun provideGetGitHubContributorListUseCase(repository: GitHubRepository) = GetGitHubContributorsUseCase(repository)

}