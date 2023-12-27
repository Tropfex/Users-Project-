package data

import domain.models.User
import domain.repository.UserRepository
import kotlinx.serialization.json.Json
import java.net.URL

class UserRepositoryImpl: UserRepository {

    override fun getUsers(): List<User> {
        val url = "https://jsonplaceholder.typicode.com/users"
        val response = URL(url).readText()

        val json = Json { ignoreUnknownKeys = true }

        return json.decodeFromString<List<User>>(response)
    }
}