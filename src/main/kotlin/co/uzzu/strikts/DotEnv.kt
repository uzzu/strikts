@file:Suppress("FunctionName")

package co.uzzu.strikts

import java.io.File
import java.io.FileNotFoundException
import kotlin.reflect.KProperty

/**
 * Creates a [DotEnv] object with .env file on working directory.
 * @param ignoreFileNotFound
 * @throws FileNotFoundException if .env file not found, and did not suppress with [ignoreFileNotFound] parameter
 */
fun DotEnv(ignoreFileNotFound: Boolean = true): DotEnv {
    val workingDir = System.getProperty("user.dir")
    return DotEnv(File(workingDir, ".env"), ignoreFileNotFound)
}

/**
 * Creates a [DotEnv] object with specified .env file.
 * @param file `.env` file
 * @param ignoreFileNotFound
 * @throws FileNotFoundException if .env file not found, and did not suppress with [ignoreFileNotFound] parameter
 */
fun DotEnv(file: File, ignoreFileNotFound: Boolean = true): DotEnv {
    if (!file.exists() || !file.isFile) {
        if (!ignoreFileNotFound) {
            throw FileNotFoundException("File not found ${file.absolutePath}")
        }
        return DotEnv("")
    }
    return DotEnv(file.readText())
}

/**
 * Creates a [DotEnv] object with specified .env text
 * @param text
 */
fun DotEnv(text: String): DotEnv {
    val envProvider = SystemEnvProvider()
    val map = DotEnvParser.parse(text)
    return DotEnv(envProvider, map)
}

class DotEnv
internal constructor(
    private val envProvider: EnvProvider,
    private val dotenvMap: Map<String, String?>,
) {

    /**
     * @return All environment variables which are merged with variables specified in .env files.
     */
    val allVariables: Map<String, String>
        get() {
            val results = envProvider.getenv().toMutableMap()
            dotenvMap.forEach { (key, value) ->
                if (value != null && results[key] == null) {
                    results[key] = value
                }
            }
            return results.toMap()
        }

    /**
     * @see [fetchOrNull]
     * @return [NullableDotEnvVariable]
     */
    val orNull: NullableDotEnvVariable by lazy { NullableDotEnvVariable(this) }

    /**
     * @see [fetch]
     * @return [DefaultValueDotEnvVariable]
     */
    fun orElse(defaultValue: String): DefaultValueDotEnvVariable =
        DefaultValueDotEnvVariable(this, defaultValue)

    /**
     * Delegate property to fetch environment variables
     * @see [fetch]
     * @throws IllegalStateException if environment variable was not set
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        fetch(property.name)

    /**
     * @return Indicates an environment variable with specified name is present
     */
    fun isPresent(name: String): Boolean =
        envProvider.getenv()[name]?.let { true }
            ?: dotenvMap[name]?.let { true }
            ?: false

    /**
     * @return The environment variable for name
     * @throws IllegalStateException if environment variable was not set
     */
    fun fetch(name: String) =
        envProvider.getenv()[name]
            ?: dotenvMap[name]
            ?: throw IllegalStateException("""Environment variable $name was not set.""")

    /**
     * @param name name for environment variable
     * @param defaultValue value to return if environment variable was not set.
     * @return The environment variable for name.
     * @return null if environment variable was not set.
     */
    fun fetch(name: String, defaultValue: String) =
        envProvider.getenv()[name]
            ?: dotenvMap[name]
            ?: defaultValue

    /**
     * @return The environment variable for name.
     * @return null if environment variable was not set.
     */
    fun fetchOrNull(name: String): String? =
        envProvider.getenv()[name]
            ?: dotenvMap[name]
}

/**
 * A .env variable class implements delegated getter property of environment variable.
 */
class NullableDotEnvVariable
internal constructor(
    private val env: DotEnv
) {
    /**
     * @return environment variable for property name
     * @return null if environment variable does not found.
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? =
        env.fetchOrNull(property.name)
}

/**
 * A .env variable class implements delegated getter property of environment variable
 */
class DefaultValueDotEnvVariable(
    private val env: DotEnv,
    private val defaultValue: String
) {
    /**
     * @return environment variable for property name
     * @return defaultValue if environment variable does not found.
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        env.fetch(property.name, defaultValue)
}
