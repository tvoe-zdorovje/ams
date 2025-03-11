package by.anatolyloyko.ams.user.action

import by.anatolyloyko.ams.orm.exposed.schemas.users.function.SaveUserFunction
import by.anatolyloyko.ams.orm.exposed.util.select
import by.anatolyloyko.ams.user.model.User
import org.springframework.stereotype.Component

@Component
internal class ExposedCreateUserAction : CreateUserAction {
    override fun invoke(user: User): Long = SaveUserFunction(
        iId = user.id,
        iFirstName = user.firstName,
        iLastName = user.lastName
    )
        .select()
}
