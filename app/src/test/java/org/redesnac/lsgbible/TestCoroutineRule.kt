package org.redesnac.lsgbible

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.redesnac.lsgbible.domain.CoroutineDispatcherProvider

class TestCoroutineRule : TestRule {

    @OptIn(ExperimentalCoroutinesApi::class)
    val testCoroutineDispatcher = StandardTestDispatcher()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun runTest(block: suspend TestScope.() -> Unit) = testCoroutineScope.runTest {
        block()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTestCoroutineDispatcherProvider() = mockk<CoroutineDispatcherProvider> {
        every { main } returns testCoroutineDispatcher
        every { io } returns testCoroutineDispatcher
    }
}