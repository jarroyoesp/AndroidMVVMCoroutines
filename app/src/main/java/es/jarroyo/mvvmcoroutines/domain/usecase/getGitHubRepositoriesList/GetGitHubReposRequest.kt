package es.jarroyo.mvvmcoroutines.domain.usecase.getGitHubRepositoriesList

import es.jarroyo.mvvmcoroutines.domain.usecase.base.BaseRequest


/**
 * Created by javierarroyo on 24/1/18.
 */
class GetGitHubReposRequest(var username: String) : BaseRequest {
    override fun validate(): Boolean {
/*
        if (!(UserValidator isEmailFormatValid (email))) {
            validator.onIncorrectEmailFormat()
            return false
        }

        if (!(UserValidator isPasswordValid (password))) {
            validator.onIncorrectPasswordFormat()
            return false
        }*/

        return true
    }

    interface Validator {
        fun onIncorrectEmailFormat()

        fun onIncorrectPasswordFormat()
    }
}