package by.anatolyloyko.ams.auth

import by.anatolyloyko.ams.auth.user.model.User

const val IDP_USER_ID = "708e421b-d2c5-42b9-9114-dc2b9109bb49"

const val USER_ID = 100000001413121100

val USER = User(
    id = USER_ID,
    idpUUID = IDP_USER_ID,
)