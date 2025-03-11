package by.anatolyloyko.ams.tasks

import by.anatolyloyko.ams.orm.util.generator.SchemaGenerator
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import org.assertj.core.api.WithAssertions
import org.gradle.api.Project
import org.junit.jupiter.api.Test
import java.io.File

class GenerateDatabaseSchemasTaskTest : WithAssertions {
    private val dbCredentials = mapOf(
        "url" to "db_url",
        "username" to "db_username",
        "password" to "db_password"
    )

    private val projectName = "projectName"
    private val projectDirectory = File("projectDir")
    private val projectGroup = "projectGroup"

    private val project = mockk<Project>() {
        every { name } returns projectName
        every { projectDir } returns projectDirectory
        every { group } returns projectGroup
    }

    private val generateDatabaseSchemasTask = GenerateDatabaseSchemasTask(project)


    @Test
    fun `must find database config and run SchemaGenerator`() {
        val taskSpy = spyk<GenerateDatabaseSchemasTask>(generateDatabaseSchemasTask, recordPrivateCalls = true) {
            every { this@spyk["findDatasourceConfig"]() } returns dbCredentials
        }
        val schemaGenerator = mockk<SchemaGenerator>(relaxed = true)

        val schema = "schema"

        mockkObject(SchemaGenerator) {
            every {
                SchemaGenerator.buildGenerator(
                    url = any(),
                    user = any(),
                    password = any(),
                    pathToDestinationModule = any(),
                    destinationPackage = any()
                )
            } returns schemaGenerator

            taskSpy.execute(schema)
        }

        verify(exactly = 1) {
            taskSpy["findDatasourceConfig"]()
            SchemaGenerator.buildGenerator(
                url = dbCredentials["url"]!!,
                user = dbCredentials["username"]!!,
                password = dbCredentials["password"]!!,
                pathToDestinationModule = projectDirectory.absolutePath,
                destinationPackage = "$projectGroup.orm.exposed.schemas",
            )
        }
    }
}
