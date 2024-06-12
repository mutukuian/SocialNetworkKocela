package com.kocelanetwork.data.repository_impl


import com.kocelanetwork.core.common.Resource
import com.kocelanetwork.data.api_service.AuthService
import com.kocelanetwork.data.api_service.LoginRequest
import com.kocelanetwork.data.api_service.RegisterRequest
import com.kocelanetwork.domain.models.User
import com.kocelanetwork.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun login(email: String, password: String): Flow<Resource<Result<User>>> = flow {
        emit(Resource.Loading())
        try {
            val response = authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    val user = User(
                        id = "", // Set appropriately if there's an id in response
                        email = email,
                        name = loginResponse.name, // Set appropriately if there's a name in response
                        token = loginResponse.token

                    )
                    emit(Resource.Success(Result.success(user)))
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Flow<Resource<Result<User>>> = flow {
        emit(Resource.Loading())
        try {
            val response = authService.register(RegisterRequest(email, password, name))
            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    val user = User(
                        id = registerResponse.userData.id,
                        email = registerResponse.userData.email,
                        name = registerResponse.userData.name,
                        token = "" // Set appropriately if there's a token in response
                    )
                    emit(Resource.Success(Result.success(user)))
                } ?: emit(Resource.Error("Unexpected error"))
            } else {
                emit(Resource.Error(response.message()))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.localizedMessage}"))
        } catch (e: HttpException) {
            emit(Resource.Error("HTTP error: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage}"))
        }
    }
}
