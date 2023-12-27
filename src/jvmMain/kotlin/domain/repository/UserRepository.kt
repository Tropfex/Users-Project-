package domain.repository

import domain.models.User

interface UserRepository {

    fun getUsers(): List<User>
}