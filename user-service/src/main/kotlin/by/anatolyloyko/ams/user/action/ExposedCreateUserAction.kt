package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.orm.exposed.schemas.users.function.CreateUserFunction
import by.anatolyloyko.ams.orm.exposed.util.select
import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Component

@Component
internal class ExposedCreateUserAction : CreateUserAction {
    override fun invoke(user: User, password: String): Long = CreateUserFunction(
        iPassword = password,
        iFirstName = user.firstName,
        iLastName = user.lastName,
        iPhoneNumber = user.phoneNumber
    )
        .select()
}
