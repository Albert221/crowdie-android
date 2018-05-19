package me.wolszon.crowdie.api.okhttp

import me.wolszon.crowdie.api.domain.GroupManager
import me.wolszon.crowdie.base.Preferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class UserTokenInterceptor(private val preferences: Preferences) : Interceptor {
    enum class TokenAppended {
        STATE, LAST, NONE
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        when (whichTokenShouldBeAppended(original)) {
            TokenAppended.STATE -> {
                val request = original.newBuilder()
                        .addHeader("Authentication", "Bearer ${getStateToken()}")
                        .build()

                return chain.proceed(request)
            }

            TokenAppended.LAST -> {
                val request = original.newBuilder()
                        .addHeader("Authentication", "Bearer ${getLastToken()}")
                        .build()

                return chain.proceed(request)
            }

            TokenAppended.NONE -> Unit
        }

        return chain.proceed(original)
    }

    private fun whichTokenShouldBeAppended(request: Request): TokenAppended {
        // Only creating and joining group is done through POST.
        if (request.method() == "POST") {
            return TokenAppended.NONE
        }

        if (GroupManager.state != null) {
            return TokenAppended.STATE
        } else if (preferences.lastJoinedGroupToken != null) {
            return TokenAppended.LAST
        }

        return TokenAppended.NONE
    }

    private fun getStateToken(): String = GroupManager.state!!.token

    private fun getLastToken(): String = preferences.lastJoinedGroupToken!!
}