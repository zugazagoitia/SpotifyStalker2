package com.zugazagoitia.spotifystalker.data


import com.zugazagoitia.spotifystalker.model.LoggedInUser
import kotlin.jvm.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository  // private constructor : singleton access
private constructor(private val dataSource: LoginDatasource) {
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private var user: LoggedInUser? = null
    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(
        username: String,
        password: String,
        rememberPassword: Boolean
    ): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            val result = dataSource.login(username, password, rememberPassword).getOrElse { return@withContext Result.failure(it) }
            user = result
            return@withContext Result.success(result)
        }
    }

    fun loginSavedCredentials(): Result<LoggedInUser> {
        val result = dataSource.loginWithSavedCredentials().getOrElse { return Result.failure(it) }
        user = result
        return Result.success(result)
    }

    companion object {
        @Volatile
        private var instance: LoginRepository? = null
        fun getInstance(dataSource: LoginDatasource): LoginRepository {
            if (instance == null) {
                instance = LoginRepository(dataSource)
            }
            return instance as LoginRepository
        }

        fun getInstance(): LoginRepository? {
            return instance
        }

        val isLoggedIn: Boolean
            get() = if (instance == null) false else instance!!.user != null
        val user: LoggedInUser?
            get() = if (instance == null) null else instance!!.user
    }
}