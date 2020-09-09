import org.gradle.api.Project

private const val majorVersion: Int = 0
private const val minorVersion: Int = 2
private const val patchVersion: Int = 0

val Project.publishingGroupId: String
    get() = "co.uzzu.strikts"

val Project.publishingArtifactId: String
    get() = "strikts"

val Project.publishingArtifactVersion: String
    get() = "$majorVersion.$minorVersion.$patchVersion"

fun Project.publishingArtifactVersion(isPublishProduction: Boolean): String =
    if (isPublishProduction) {
        publishingArtifactVersion
    } else {
        "$publishingArtifactVersion-SNAPSHOT"
    }

object MavenPublications {
    const val description = "kotlin-main-kts helpers"
    const val url = "https://github.com/uzzu/strikts"
    const val license = "The Apache Software License, Version 2.0"
    const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    const val licenseDistribution = "repo"
    const val developersId = "uzzu"
    const val developersName = "Hirokazu Uzu"
    const val organization = developersId
    const val organizationUrl = "https://uzzu.co"
    const val scmUrl = "https://github.com/uzzu/strikts"
}
