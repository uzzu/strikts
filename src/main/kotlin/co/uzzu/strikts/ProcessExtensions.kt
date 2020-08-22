package co.uzzu.strikts

import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * [Process.waitFor] with suspending function
 */
suspend fun Process.suspendingWaitFor(): Int = suspendCoroutine {
    runCatching { waitFor() }
        .onSuccess { code -> it.resume(code) }
        .onFailure { e -> it.resumeWithException(e) }
}

/**
 * [Process.waitFor] with suspending function
 */
suspend fun Process.suspendingWaitForTimeout(delay: Long, unit: TimeUnit): Boolean = suspendCoroutine {
    runCatching { waitFor(delay, unit) }
        .onSuccess { isCompleted -> it.resume(isCompleted) }
        .onFailure { e -> it.resumeWithException(e) }
}
