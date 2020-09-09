package co.uzzu.strikts

import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DotEnvTest {

    @Test
    fun isPresentByEnv() {
        val envProvider = InMemoryEnvProvider(mapOf("FOO" to "env"))
        val dotenv = DotEnv(envProvider, mapOf())
        assertTrue(dotenv.isPresent("FOO"))
    }

    @Test
    fun isPresentByDotEnv() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertTrue(dotenv.isPresent("FOO"))
    }

    @Test
    fun isNotPresent() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())
        assertFalse(dotenv.isPresent("FOO"))
    }

    @Test
    fun fetchFromEnv() {
        val envProvider = InMemoryEnvProvider(mapOf("FOO" to "env"))
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("env", dotenv.fetch("FOO"))
    }

    @Test
    fun fetchFromDotEnv() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("dotenv", dotenv.fetch("FOO"))
    }

    @Test
    fun throwIfNoVariables() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())
        assertThrows(IllegalStateException::class.java) { dotenv.fetch("FOO") }
    }

    @Test
    fun fetchOrElseFromEnv() {
        val envProvider = InMemoryEnvProvider(mapOf("FOO" to "env"))
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("env", dotenv.fetch("FOO", "default"))
    }

    @Test
    fun fetchOrElseFromDotEnv() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("dotenv", dotenv.fetch("FOO", "default"))
    }

    @Test
    fun fetchOrElse() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())
        assertEquals("default", dotenv.fetch("FOO", "default"))
    }

    @Test
    fun fetchOrNullFromEnv() {
        val envProvider = InMemoryEnvProvider(mapOf("FOO" to "env"))
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("env", dotenv.fetchOrNull("FOO"))
    }

    @Test
    fun fetchOrNullFromDotEnv() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))
        assertEquals("dotenv", dotenv.fetchOrNull("FOO"))
    }

    @Test
    fun fetchOrNull() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())
        assertNull(dotenv.fetchOrNull("FOO"))
    }

    @Test
    fun allVariables() {
        val envProvider = InMemoryEnvProvider(
            mapOf(
                "FOO" to "env",
                "BAR" to "env"
            )
        )
        val dotenv = DotEnv(
            envProvider,
            mapOf(
                "BAR" to "dotenv",
                "BAZ" to "dotenv",
                "QUX" to "dotenv",
                "QUUX" to null
            )
        )
        val allVariables = dotenv.allVariables
        assertAll(
            { assertEquals("env", allVariables["FOO"]) },
            { assertEquals("env", allVariables["BAR"]) },
            { assertEquals("dotenv", allVariables["BAZ"]) },
            { assertEquals("dotenv", allVariables["QUX"]) },
            { assertEquals(null, allVariables["QUUX"]) },
            { assertEquals(null, allVariables["CORGE"]) }
        )
    }

    @Test
    fun returnsByDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf("FOO" to "env"))
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))

        @Suppress("LocalVariableName")
        val FOO by dotenv
        assertEquals("env", FOO)
    }

    @Test
    fun throwsByDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())
        assertThrows(IllegalStateException::class.java) {
            @Suppress("LocalVariableName")
            val FOO by dotenv
            doSomething(FOO)
        }
    }

    @Test
    fun returnsByOrNullDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))

        @Suppress("LocalVariableName")
        val FOO by dotenv.orNull
        assertEquals("dotenv", FOO)
    }

    @Test
    fun returnsNullByOrNullDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())

        @Suppress("LocalVariableName")
        val FOO by dotenv.orNull
        assertNull(FOO)
    }

    @Test
    fun returnsByOrElseDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf("FOO" to "dotenv"))

        @Suppress("LocalVariableName")
        val FOO by dotenv.orElse("default")
        assertEquals("dotenv", FOO)
    }

    @Test
    fun returnsDefaultValueByOrElseDelegate() {
        val envProvider = InMemoryEnvProvider(mapOf())
        val dotenv = DotEnv(envProvider, mapOf())

        @Suppress("LocalVariableName")
        val FOO by dotenv.orElse("default")
        assertEquals("default", FOO)
    }
}

private fun doSomething(string: String) {
}
