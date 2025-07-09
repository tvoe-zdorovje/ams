package by.anatolyloyko.ams.administration.user.finder

import by.anatolyloyko.ams.administration.BRAND_ID
import by.anatolyloyko.ams.administration.JooqTest
import by.anatolyloyko.ams.administration.STUDIO_ID
import by.anatolyloyko.ams.administration.USER_ID
import by.anatolyloyko.ams.administration.role.model.Role
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.BrandRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.BrandRolesRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.RoleRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.StudioRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.StudioRolesRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.UserRecord
import by.anatolyloyko.ams.orm.jooq.schemas.tables.records.UserRolesRecord
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class JooqUserRolesFinderTest : JooqTest() {
    @Autowired
    private lateinit var finder: JooqUserRolesFinder

    private val allRoles = List(5) {
        Role(it.toLong(), "name-$it", "description-$it")
    }

    private val brandRoles = allRoles.subList(0, 2)

    private val studioRoles = allRoles.subList(2, 4)

    @BeforeAll
    fun beforeAll() {
        setUpDatabase()
    }

    @Test
    fun `must find user roles by brand ID`() {
        val actualBrandRoles = finder.findByOrganizationId(
            userId = USER_ID,
            organizationId = BRAND_ID
        )

        assertThat(actualBrandRoles).isEqualTo(brandRoles)
    }

    @Test
    fun `must find user roles by studio ID`() {
        val actualStudioRoles = finder.findByOrganizationId(
            userId = USER_ID,
            organizationId = STUDIO_ID
        )

        assertThat(actualStudioRoles).isEqualTo(studioRoles)
    }

    private fun setUpDatabase() {
        with(dslContext) {
            val secondEntityId: Long = -1
            executeInsert(UserRecord(USER_ID))
            executeInsert(UserRecord(secondEntityId))

            executeInsert(BrandRecord(BRAND_ID))
            executeInsert(BrandRecord(secondEntityId))

            executeInsert(StudioRecord(STUDIO_ID))
            executeInsert(StudioRecord(secondEntityId))

            allRoles.forEach {
                executeInsert(RoleRecord(it.id!!, it.name, it.description))
                executeInsert(UserRolesRecord(secondEntityId, it.id))
            }

            brandRoles.forEach {
                executeInsert(BrandRolesRecord(BRAND_ID, it.id))
                executeInsert(BrandRolesRecord(secondEntityId, it.id))
            }

            studioRoles.forEach {
                executeInsert(StudioRolesRecord(STUDIO_ID, it.id))
                executeInsert(StudioRolesRecord(secondEntityId, it.id))
            }

            (brandRoles + studioRoles).forEach {
                executeInsert(UserRolesRecord(USER_ID, it.id))
            }
        }
    }
}
