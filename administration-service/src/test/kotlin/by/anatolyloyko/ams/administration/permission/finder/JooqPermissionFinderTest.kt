package by.anatolyloyko.ams.administration.permission.finder

import by.anatolyloyko.ams.administration.JooqTest
import by.anatolyloyko.ams.administration.PERMISSION
import by.anatolyloyko.ams.administration.ROLE
import by.anatolyloyko.ams.administration.permission.model.Permission
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.PermissionRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.RolePermissionsRecord
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JooqPermissionFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqPermissionFinder

    @BeforeAll
    fun beforeAll() {
        dslContext.executeInsert(PermissionRecord(PERMISSION.id, PERMISSION.name, PERMISSION.description))
    }

    @Test
    fun `must find and return all permissions sorted by id`() {
        val testPermissions = List(5) {
            Permission(
                id = it + 5000L,
                name = "Permission $it",
                description = "Test permission $it",
            )
        }

        dslContext
            .batchInsert(testPermissions.map { PermissionRecord(it.id, it.name, it.description) })
            .execute()

        val result = finder.findAll()

        val expectedResult = (testPermissions + PERMISSION).sortedBy(Permission::id)
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `must return empty list if no roles found by role id`() {
        val result = finder.findByRoleId(Long.MIN_VALUE)

        assertThat(result).isEmpty()
    }

    @Test
    fun `must find and return permissions by role id`() {
        dslContext.executeInsert(RolePermissionsRecord(ROLE.id, PERMISSION.id))

        val result = finder.findByRoleId(ROLE.id!!)

        assertThat(result).containsOnly(PERMISSION)
    }
}
