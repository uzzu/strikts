package co.uzzu.strikts

/**
 * @see checkNotNull
 */
fun <T : Any> T?.checkNotNull(): T =
    checkNotNull(this)

/**
 * @see checkNotNull
 */
fun <T : Any> T?.checkNotNull(lazyMessage: (() -> String)): T =
    checkNotNull(this, lazyMessage)

/**
 * @see requireNotNull
 */
fun <T : Any> T?.requireNotNull(): T =
    requireNotNull(this)

/**
 * @see requireNotNull
 */
fun <T : Any> T?.requireNotNull(lazyMessage: (() -> String)): T =
    requireNotNull(this, lazyMessage)
