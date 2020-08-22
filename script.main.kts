@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
@file:DependsOn("co.uzzu.strikts:strikts:0.1.0")

import co.uzzu.strikts.exec
import co.uzzu.strikts.glob
import kotlinx.coroutines.runBlocking

// exec
runBlocking {
    val result = "curl https://www.google.com".exec()
    println("Result: $result")
}

// glob matcher
"**/build/**/*.xml".glob().forEach {
    println(it)
}
