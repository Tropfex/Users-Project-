package domain.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    val id: Long = Random().nextLong(),
    val name: String,
    val username: String,
    val email: String
)