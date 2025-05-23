package by.anatolyloyko.ams.user.finder

import by.anatolyloyko.ams.common.infrastructure.service.finder.ExposedFinder
import by.anatolyloyko.ams.orm.exposed.schemas.users.table.UserTable
import by.anatolyloyko.ams.user.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.springframework.stereotype.Component

private val USER_MAPPER: (ResultRow) -> User = {
    User(
        id = it[UserTable.id],
        firstName = it[UserTable.firstName],
        lastName = it[UserTable.lastName],
        phoneNumber = it[UserTable.phoneNumber],
    )
}

@Component
internal class ExposedUserFinder : UserFinder, ExposedFinder<User>() {
    override fun findById(id: Long) = fetchSingle {
        UserTable
            .selectAll()
            .where { UserTable.id eq id }
    } mapUsing USER_MAPPER
}
