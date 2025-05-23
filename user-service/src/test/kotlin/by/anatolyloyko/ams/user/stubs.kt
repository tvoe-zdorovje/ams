package by.anatolyloyko.ams.user

import by.anatolyloyko.ams.user.model.User

const val USER_ID = 100000001413121100

const val USER_PASSWORD = "strong_password"

val NEW_USER = User(
    firstName = "Alexey",
    lastName = "Kasimov",
    phoneNumber = "+375297671245",
)

val USER = NEW_USER.copy(id = USER_ID)
