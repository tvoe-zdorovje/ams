package by.anatolyloyko.ams.user

import by.anatolyloyko.ams.user.model.User

const val USER_ID = 1L

val NEW_USER = User(
    firstName = "Alexey",
    lastName = "Kasimov",
)

val USER = NEW_USER.copy(id = USER_ID)
