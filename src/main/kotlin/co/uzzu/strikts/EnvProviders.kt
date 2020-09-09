package co.uzzu.strikts

/**
 * Provides environment variable
 */
internal interface EnvProvider {
    /**
     * @return A environment variable for name.
     */
    fun getenv(name: String): String?

    /**
     * @return All environment variables.
     */
    fun getenv(): Map<String, String>
}

/**
 * EnvProvider implementation using System#getenv
 */
internal class SystemEnvProvider : EnvProvider {
    override fun getenv(name: String): String? = System.getenv(name)
    override fun getenv(): Map<String, String> = System.getenv().filterValues { it != null }
}
