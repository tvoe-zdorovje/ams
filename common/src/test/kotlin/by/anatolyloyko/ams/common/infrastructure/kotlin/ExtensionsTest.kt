package by.anatolyloyko.ams.common.infrastructure.kotlin

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

class ExtensionsTest : WithAssertions {
    @Test
    fun `test alsoIf extension`() {
        val mutableList = mutableListOf<Int>()
        val block: (MutableList<Int>) -> Unit = { it += 1 }

        assertThat(mutableList.alsoIf(false, block)).isEmpty()
        assertThat(mutableList.alsoIf(true, block)).hasSize(1)
    }
}
