package org.redesnac.lsgbible.domain

import assertk.assertions.isEqualTo
import kotlinx.coroutines.Dispatchers
import org.junit.Test

class CoroutineDispatcherProviderTest {
    @Test
    fun `verify coroutine dispatchers`() {
        // When
        val result = CoroutineDispatcherProvider()

        // Then
        assertk.assertThat(result.main).isEqualTo(Dispatchers.Main)
        assertk.assertThat(result.io).isEqualTo(Dispatchers.IO)
    }
}