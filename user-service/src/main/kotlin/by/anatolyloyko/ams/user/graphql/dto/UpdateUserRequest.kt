package by.anatolyloyko.ams.user.graphql.dto

data class UpdateUserRequest(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
) : UserRequest {
    override fun getUserId(): Long = id
}
