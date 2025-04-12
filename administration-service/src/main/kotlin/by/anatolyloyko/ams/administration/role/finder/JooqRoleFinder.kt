package by.anatolyloyko.ams.administration.role.finder

import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.RoleRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.references.ROLE
import by.anatolyloyko.ams.orm.jooq.util.eq
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val MAPPER: (RoleRecord) -> Role = {
    Role(
        id = it.id,
        name = it.name,
        description = it.description
    )
}

@Component
@Transactional(readOnly = true)
class JooqRoleFinder(
    private val dsl: DSLContext
) : RoleFinder {
    override fun findById(id: Long): Role? = dsl
        .selectFrom(ROLE)
        .where(ROLE.ID eq id)
        .fetchOne(MAPPER)
}
