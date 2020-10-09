@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
@file:DependsOn("co.uzzu.strikts:strikts:0.2.0")

import co.uzzu.strikts.exec
import co.uzzu.strikts.glob
import co.uzzu.strikts.DotEnv
import kotlinx.coroutines.runBlocking

// Exec
runBlocking {
    val result = "curl https://www.google.com".exec()
    println("[Exec] Result: $result")
}

// Glob matcher
"**/*a*".glob().forEach {
    println("[Glob matcher] $it")
}

// .env variables
val env = DotEnv()

// .env delegate
val LANG by env
println("[DotEnv delegate] LANG: $LANG")
val FOO by env
println("[DotEnv delegate] foo: $FOO")
val BAR by env.orNull
println("[DotEnv delegate] bar: $BAR")
val BAZ by env.orElse("default falue")
println("[DotEnv delegate] baz: $BAZ")

// .env fetch
println("""[Dotenv fetch] LANG is present? ${env.isPresent("LANG")}""")
println("""[Dotenv fetch] LANG: ${env.fetch("LANG")}""")
println("""[Dotenv fetch] BAR: ${env.fetchOrNull("BAR")}""")
println("""[Dotenv fetch] BAZ: ${env.fetch("BAZ", "default value")}""")

