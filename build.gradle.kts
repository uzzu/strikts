import com.jfrog.bintray.gradle.BintrayExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.0"
    `java-library`
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

repositories {
    jcenter()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

group = publishingGroupId
version = publishingArtifactVersion
setProperty("archivesBaseName", publishingArtifactId)

publishing {
    repositories {
        maven {
            name = "bintray"
            url = uri("https://api.bintray.com/content/$bintrayUser/${Bintray.repo}/${Bintray.packageName}/$publishingArtifactVersion;override=1;publish=0") // ktlint-disable max-line-length
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))
            groupId = publishingGroupId
            artifactId = publishingArtifactId
            version = publishingArtifactVersion
            artifact(sourcesJar)

            pom {
                name.set(publishingArtifactId)
                description.set(MavenPublications.description)
                url.set(MavenPublications.url)
                licenses {
                    license {
                        name.set(MavenPublications.license)
                        url.set(MavenPublications.licenseUrl)
                        distribution.set(MavenPublications.licenseDistribution)
                    }
                }
                developers {
                    developer {
                        id.set(MavenPublications.developersId)
                        name.set(MavenPublications.developersName)
                        organization.set(MavenPublications.organization)
                        organizationUrl.set(MavenPublications.organizationUrl)
                    }
                }
                scm {
                    url.set(MavenPublications.scmUrl)
                }
            }
        }
    }
}

bintray {
    user = bintrayUser
    key = bintrayApiKey
    publish = false
    setPublications(
        *publishing.publications
            .withType<MavenPublication>()
            .map { it.name }
            .toTypedArray()
    )
    pkg(
        delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = Bintray.repo
            name = Bintray.packageName
            desc = Bintray.desc
            userOrg = Bintray.userOrg
            websiteUrl = Bintray.websiteUrl
            vcsUrl = Bintray.vcsUrl
            issueTrackerUrl = Bintray.issueTrackerUrl
            githubRepo = Bintray.githubRepo
            githubReleaseNotesFile = Bintray.githubReleaseNoteFile
            setLabels(* Bintray.labels)
            setLicenses(*Bintray.licenses)
            version(
                delegateClosureOf<BintrayExtension.VersionConfig> {
                    name = publishingArtifactVersion
                }
            )
        }
    )
}
