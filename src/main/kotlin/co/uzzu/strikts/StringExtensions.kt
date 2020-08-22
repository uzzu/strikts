package co.uzzu.strikts

import java.io.InputStreamReader
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

/**
 * Execute shell command
 * @return stdout
 */
suspend fun String.exec(): String {
    val commands = this.trim().split("\\s".toRegex())
    val process = ProcessBuilder(*commands.toTypedArray())
        .start()
        .requireNotNull()
    return try {
        if (process.suspendingWaitFor() != 0) {
            throw Exception(InputStreamReader(process.errorStream).use { it.readText() })
        }
        InputStreamReader(process.inputStream).use { it.readText() }
    } finally {
        process.errorStream.close()
        process.inputStream.close()
        process.outputStream.close()
        process.destroyForcibly()
    }
}

/**
 * Collect file path with glob matcher
 */
fun String.glob(basePath: String = System.getProperty("user.dir")): Stream<Path> {
    val pattern = if (!this.startsWith("glob:")) {
        "glob:$this"
    } else {
        this
    }

    val matcher = FileSystems.getDefault().getPathMatcher(pattern)
    return Files.walk(Paths.get(basePath)).filter(matcher::matches)
}
