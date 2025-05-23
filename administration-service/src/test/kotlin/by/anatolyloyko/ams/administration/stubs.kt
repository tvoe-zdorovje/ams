package by.anatolyloyko.ams.administration

import by.anatolyloyko.ams.administration.permission.model.Permission
import by.anatolyloyko.ams.administration.role.model.Role

const val USER_ID = 1000000001413121100

const val BRAND_ID = 1000000001513221100

const val STUDIO_ID = 1000000000171342100

const val ROLE_ID = 1000000001613321100

val NEW_ROLE = Role(
    name = "Test Role",
    description = "The role for testing",
)

val ROLE = NEW_ROLE.copy(id = ROLE_ID)

val PERMISSION = Permission(
    id = 1000,
    name = "Test_Permission",
    description = "Test Permission"
)
